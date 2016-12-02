package se.sead.bugsimport.speciessynonyms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.Persister;
import se.sead.bugsimport.species.seadmodel.TaxaAuthor;
import se.sead.bugsimport.species.seadmodel.TaxaGenus;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.bugsimport.speciesassociation.seadmodel.SpeciesAssociation;
import se.sead.bugsimport.speciessynonyms.bugsmodel.Synonym;
import se.sead.repositories.SpeciesAssociationRepository;
import se.sead.repositories.SpeciesRepository;
import se.sead.repositories.TaxaAuthorRepository;
import se.sead.repositories.TaxaGenusRepository;

import java.util.HashMap;
import java.util.Map;

@Component
public class SynonymPersister extends Persister<Synonym, SpeciesAssociation> {

    @Autowired
    private SpeciesAssociationRepository repository;
    @Autowired
    private TaxaGenusRepository genusRepository;
    @Autowired
    private SpeciesRepository speciesRepository;
    @Autowired
    private TaxaAuthorRepository authorRepository;

    private SourceSpeciesPersister sourceSpeciesPersister;

    void init(){
        sourceSpeciesPersister = new SourceSpeciesPersister(speciesRepository, genusRepository, authorRepository);
    }

    void clear(){
        sourceSpeciesPersister.clear();
    }

    @Override
    protected SpeciesAssociation save(SpeciesAssociation seadValue) {
        if(seadValue.isNewItem()){
            TaxaSpecies sourceSpecies = sourceSpeciesPersister.save(seadValue.getSourceSpecies());
            seadValue.setSourceSpecies(sourceSpecies);
        }
        return repository.saveOrUpdate(seadValue);
    }

    private static class SourceSpeciesPersister {

        private Map<String, TaxaSpecies> persistedSpecies;
        private SourceGenusPersister genusPersister;
        private SourceAuthorPersister authorPersister;
        private SpeciesRepository speciesRepository;

        SourceSpeciesPersister(
                SpeciesRepository speciesRepository,
                TaxaGenusRepository genusRepository,
                TaxaAuthorRepository authorRepository){
            genusPersister = new SourceGenusPersister(genusRepository);
            authorPersister = new SourceAuthorPersister(authorRepository);
            persistedSpecies = new HashMap<>();
            this.speciesRepository = speciesRepository;
        }

        void clear(){
            persistedSpecies.clear();
            genusPersister.clear();
            authorPersister.clear();
        }

        TaxaSpecies save(TaxaSpecies species){
            if(!species.isNewItem()){
                return species;
            } else {
                String key = createKey(species);
                if (persistedSpecies.containsKey(key)) {
                    return persistedSpecies.get(key);
                } else {
                    species.setGenus(genusPersister.save(species.getGenus()));
                    species.setAuthor(authorPersister.save(species.getAuthor()));
                    TaxaSpecies savedSpecies = speciesRepository.saveOrUpdate(species);
                    persistedSpecies.put(key, savedSpecies);
                    return savedSpecies;
                }
            }
        }

        private String createKey(TaxaSpecies species){
            return species.getSpeciesName() +
                    species.getGenus().getGenusName() +
                    (species.getAuthor() != null ? species.getAuthor().getAuthorName() : "");
        }
    }

    private static class SourceGenusPersister {

        private TaxaGenusRepository repository;
        private Map<String, TaxaGenus> persistedGenera;

        SourceGenusPersister(TaxaGenusRepository repository){
            this.repository = repository;
            persistedGenera = new HashMap<>();
        }

        void clear(){
            persistedGenera.clear();
        }

        TaxaGenus save(TaxaGenus genus){
            if(!genus.isNewItem()){
                return genus;
            } else {
                String key = createKey(genus);
                if(persistedGenera.containsKey(key)){
                    return persistedGenera.get(key);
                } else {
                    TaxaGenus persistedGenus = repository.saveOrUpdate(genus);
                    persistedGenera.put(key, persistedGenus);
                    return persistedGenus;
                }
            }
        }

        private String createKey(TaxaGenus genus){
            return genus.getFamily().getFamilyName() +
                    genus.getGenusName();
        }
    }

    private static class SourceAuthorPersister {
        private TaxaAuthorRepository repository;
        private Map<String, TaxaAuthor> persistedAuthors;

        SourceAuthorPersister(
                TaxaAuthorRepository repository
        ){
            this.repository = repository;
            persistedAuthors = new HashMap<>();
        }

        void clear(){
            persistedAuthors.clear();
        }

        TaxaAuthor save(TaxaAuthor sourceAuthor){
            if(sourceAuthor == null || !sourceAuthor.isNewItem()){
                return sourceAuthor;
            } else {
                if(persistedAuthors.containsKey(sourceAuthor.getAuthorName())){
                    return persistedAuthors.get(sourceAuthor.getAuthorName());
                } else {
                    TaxaAuthor persistedAuthor = repository.saveOrUpdate(sourceAuthor);
                    persistedAuthors.put(sourceAuthor.getAuthorName(), persistedAuthor);
                    return persistedAuthor;
                }
            }
        }
    }
}
