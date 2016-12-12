package se.sead.bugsimport.species;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.Persister;
import se.sead.bugsimport.species.bugsmodel.INDEX;
import se.sead.bugsimport.species.converters.NoDataSpeciesConverter;
import se.sead.bugsimport.species.seadmodel.*;
import se.sead.repositories.*;

@Component
public class TaxonomicOrderPersister extends Persister<INDEX, TaxonomicOrder> {

    @Autowired
    private TaxonomicOrderRepository taxonomicOrderRepository;
    @Autowired
    private TaxaFamilyRepository familyRepository;
    @Autowired
    private TaxaGenusRepository genusRepository;
    @Autowired
    private SpeciesRepository speciesRepository;
    @Autowired
    private TaxaAuthorRepository authorRepository;
    @Autowired
    private NoDataSpeciesConverter noDataSpeciesConverter;

    @Override
    protected TaxonomicOrder save(TaxonomicOrder seadValue) {
        SpeciesTreeUpdater updater = new SpeciesTreeUpdater();
        updater.updateTaxonomyOrder(seadValue);
        TaxonomicOrder savedOrder = taxonomicOrderRepository.saveOrUpdate(seadValue);
        return savedOrder;
    }

    private class SpeciesTreeUpdater {

        void updateTaxonomyOrder(TaxonomicOrder seadValue){
            if(!seadValue.isNewItem()){
                return;
            }
            seadValue.setSpecies(updateSpecies(seadValue.getSpecies()));
        }

        private TaxaSpecies updateSpecies(TaxaSpecies species){
            if(noDataSpeciesConverter.isNoDataSpecies(species)){
                return species;
            }
            TaxaGenus dbGenus = updateGenus(species.getGenus());
            if(dbGenus.isNewItem()){
                return species;
            }
            species.setGenus(dbGenus);
            TaxaAuthor dbAuthor = updateAuthor(species.getAuthor());
            if(dbAuthor != null && dbAuthor.isNewItem()){
                return species;
            }
            species.setAuthor(dbAuthor);
            TaxaSpecies cacheSpecies = speciesRepository.findBySpeciesNameAndGenusGenusNameAndAuthorAuthorName(
                    species.getSpeciesName(),
                    species.getGenus().getGenusName(),
                    (species.getAuthor() != null ? species.getAuthor().getAuthorName() : null)
            );
            if(cacheSpecies != null){
                return cacheSpecies;
            }
            return species;
        }

        private TaxaGenus updateGenus(TaxaGenus genus){
            TaxaFamily dbFamily = updateFamily(genus.getFamily());
            if(dbFamily.isNewItem()){
                return genus;
            }
            genus.setFamily(dbFamily);
            TaxaGenus dbGenus = genusRepository.findByGenusNameAndFamily(genus.getGenusName(), genus.getFamily());
            if (dbGenus != null) {
                return dbGenus;
            }
            return genus;
        }

        private TaxaFamily updateFamily(TaxaFamily family) {
            TaxaFamily cacheFamily = familyRepository.findByFamilyNameAndOrder(family.getFamilyName(), family.getOrder());
            if (cacheFamily != null) {
                return cacheFamily;
            }
            return family;
        }

        private TaxaAuthor updateAuthor(TaxaAuthor author){
            if(author == null){
                return null;
            }
            TaxaAuthor cachedAuthor = authorRepository.findByAuthorName(author.getAuthorName());
            if(cachedAuthor != null){
                return cachedAuthor;
            }
            return author;
        }
    }
}
