package se.sead.bugsimport.speciessynonyms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.species.seadmodel.TaxaAuthor;
import se.sead.bugsimport.species.seadmodel.TaxaFamily;
import se.sead.bugsimport.species.seadmodel.TaxaGenus;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.bugsimport.speciessynonyms.bugsmodel.Synonym;
import se.sead.repositories.SpeciesRepository;
import se.sead.repositories.TaxaAuthorRepository;
import se.sead.repositories.TaxaGenusRepository;
import se.sead.utils.ErrorCopier;

import java.util.*;

@Component
public class SynonymSpeciesManager {

    private final TaxaGenus NO_GENUS_SPECIFIED;

    @Autowired
    private GenusManager genusManager;
    @Autowired
    private SynonymSpeciesCache speciesCache;

    public SynonymSpeciesManager(){
        NO_GENUS_SPECIFIED = new TaxaGenus();
        NO_GENUS_SPECIFIED.addError("No synonym genus specified");
    }

    public void clear(){
        genusManager.clear();
        speciesCache.clear();
    }

    public TaxaSpecies getOrCreate(TaxaSpecies targetSpecies, Synonym synonym){
        return new SynonymMapper(targetSpecies, synonym).getSynonymSpecies();
    }

    private class SynonymMapper {
        private TaxaSpecies targetSpecies;
        private Synonym synonym;

        SynonymMapper(TaxaSpecies targetSpecies, Synonym synonym) {
            this.targetSpecies = targetSpecies;
            this.synonym = synonym;
        }

        TaxaSpecies getSynonymSpecies(){
            TaxaGenus synonymGenus = getGenus();
            TaxaSpecies synonymSpecies = getOrCreateFromSynonymGenus(synonymGenus);
            return synonymSpecies;
        }

        private TaxaGenus getGenus(){
            if(synonym.getSynGenus() == null){
                return NO_GENUS_SPECIFIED;
            }
            return genusManager.getOrCreateGenus(targetSpecies.getGenus().getTaxaFamily(), synonym.getSynGenus());
        }

        private TaxaSpecies getOrCreateFromSynonymGenus(TaxaGenus synonymGenus){
            if(!synonymGenus.isErrorFree()){
                TaxaSpecies error = new TaxaSpecies();
                ErrorCopier.copyPotentialErrors(error, synonymGenus);
                return error;
            }
            return speciesCache.getOrCreateSynonymSpecies(synonymGenus, synonym, targetSpecies);
        }
    }

    @Component
    public static class GenusManager {

        private Map<String, TaxaGenus> cache = new HashMap<>();

        @Autowired
        private TaxaGenusRepository genusRepository;

        void clear(){
            cache.clear();
        }

        TaxaGenus getOrCreateGenus(TaxaFamily targetFamily, String genusName){
            String key = constructKey(targetFamily, genusName);
            TaxaGenus taxaGenus = cache.get(key);
            if(taxaGenus == null){
                taxaGenus = getOrCreate(targetFamily, genusName);
                cache.put(key, taxaGenus);
            }
            return taxaGenus;
        }

        private String constructKey(TaxaFamily targetFamily, String genusName) {
            return targetFamily.getFamilyName() + "-" + genusName;
        }

        private TaxaGenus getOrCreate(TaxaFamily targetFamily, String genusName){
            TaxaGenus synonymGenus = genusRepository.findByGenusNameAndTaxaFamily(genusName, targetFamily);
            if(synonymGenus == null){
                return createGenus(genusName, targetFamily);
            } else {
                return synonymGenus;
            }
        }

        private TaxaGenus createGenus(String synonymName, TaxaFamily targetFamily){
            TaxaGenus synonymGenus = new TaxaGenus();
            synonymGenus.setGenusName(synonymName);
            synonymGenus.setTaxaFamily(targetFamily);
            return synonymGenus;
        }
    }

    @Component
    public static class SynonymSpeciesCache {
        private Map<String, TaxaSpecies> cache = new HashMap<>();
        @Autowired
        private SpeciesRepository speciesRepository;
        @Autowired
        private SynonymAuthorityManager authorityManager;

        void clear(){
            cache.clear();
            authorityManager.clear();
        }

        TaxaSpecies getOrCreateSynonymSpecies(TaxaGenus synonymGenus, Synonym synonym, TaxaSpecies targetSpecies){
            String key = constructKey(synonymGenus.getGenusName(), synonym, targetSpecies);
            TaxaSpecies fromCache = cache.get(key);
            if(fromCache == null){
                fromCache = getOrCreate(synonymGenus, synonym, targetSpecies);
                cache.put(key, fromCache);
            }
            return fromCache;
        }

        private String constructKey(String genusName, Synonym synonym, TaxaSpecies fallbackInfo) {
            String authority = synonym.getSynAuthority() != null ? synonym.getSynAuthority() : "NO_AUTHORITY";
            String speciesSynonym = synonym.getSynSpecies() != null ? synonym.getSynSpecies() : fallbackInfo.getSpeciesName();
            return genusName + "-" + speciesSynonym + "-" + authority;
        }

        private TaxaSpecies getOrCreate(TaxaGenus synonymGenus, Synonym synonym, TaxaSpecies targetSpecies){
            String name = synonym.getSynSpecies() != null ? synonym.getSynSpecies() : targetSpecies.getSpeciesName();
            String authorName = targetSpecies.getTaxaAuthor() != null ? targetSpecies.getTaxaAuthor().getAuthorName() : null;
            TaxaSpecies found = speciesRepository.findBySpeciesNameAndGenusGenusNameAndTaxaAuthorAuthorName(name, synonymGenus.getGenusName(), authorName);
            if(found == null || !authorityManager.authorityMatches(found, targetSpecies)){
                return createBaseSynonymSpecies(synonymGenus, synonym, targetSpecies);
            } else {
                return found;
            }
        }

        private TaxaSpecies createBaseSynonymSpecies(TaxaGenus synonymGenus, Synonym synonym, TaxaSpecies targetSpecies){
            TaxaSpecies synonymSpecies = new TaxaSpecies();
            synonymSpecies.setGenus(synonymGenus);
            if(synonym.getSynSpecies() == null){
                synonymSpecies.setSpeciesName(targetSpecies.getSpeciesName());
            } else {
                synonymSpecies.setSpeciesName(synonym.getSynSpecies());
            }
            authorityManager.update(synonymSpecies, synonym);
            return synonymSpecies;
        }
    }

    @Component
    public static class SynonymAuthorityManager {
        @Autowired
        private TaxaAuthorRepository authorRepository;

        private Map<String, TaxaAuthor> cache = new HashMap<>();

        void clear(){
            cache.clear();
        }

        boolean authorityMatches(TaxaSpecies dbBackedSynonym, TaxaSpecies targetSpecies){
            return Objects.equals(dbBackedSynonym.getTaxaAuthor(), targetSpecies.getTaxaAuthor());
        }

        void update(TaxaSpecies synonymSpecies, Synonym synonym){
            if(synonym.getSynAuthority() == null){
                return;
            }
            String key = createKey(synonym);
            TaxaAuthor cachedAuthor = cache.get(key);
            if(cachedAuthor == null){
                cachedAuthor = getOrCreate(synonym);
                cache.put(key, cachedAuthor);
            }
            synonymSpecies.setTaxaAuthor(cachedAuthor);
        }

        private String createKey(Synonym synonym) {
            return synonym.getSynAuthority();
        }

        private TaxaAuthor getOrCreate(Synonym synonym){
            TaxaAuthor authorityFromSynonym = authorRepository.findByAuthorName(synonym.getSynAuthority());
            if (authorityFromSynonym == null) {
                authorityFromSynonym = createAuthor(synonym);
            }
            return authorityFromSynonym;
        }

        private TaxaAuthor createAuthor(Synonym synonym) {
             TaxaAuthor authorityFromSynonym = new TaxaAuthor();
            authorityFromSynonym.setAuthorName(synonym.getSynAuthority());
            return authorityFromSynonym;
        }
    }
}
