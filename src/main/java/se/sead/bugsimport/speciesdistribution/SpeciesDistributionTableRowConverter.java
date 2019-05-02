package se.sead.bugsimport.speciesdistribution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.BugsTableRowConverter;
import se.sead.bugsimport.species.converters.TaxonomicOrderConverter;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.bugsimport.species.seadmodel.TaxonomicOrder;
import se.sead.bugsimport.speciesdistribution.bugsmodel.Distrib;
import se.sead.bugsimport.speciesdistribution.seadmodel.TextDistribution;
import se.sead.repositories.BiblioDataRepository;
import se.sead.repositories.TextDistributionRepository;
import se.sead.bugsimport.bibliography.seadmodel.Biblio;

@Component
public class SpeciesDistributionTableRowConverter implements BugsTableRowConverter<Distrib, TextDistribution> {

    @Autowired
    private TaxonomicOrderConverter orderConverter;
    @Autowired
    private BiblioDataRepository biblioDataRepository;
    @Autowired
    private TextDistributionRepository distributionRepository;

    @Override
    public TextDistribution convertForDataRow(Distrib bugsData) {
        TaxaSpecies species = getSpecies(bugsData.getCode());
        Biblio reference = getReference(bugsData.getRef());
        return getOrCreate(bugsData, species, reference);
    }

    private Biblio getReference(String reference) {
        return biblioDataRepository.getByBugsReferenceIgnoreCase(reference);
    }

    private TaxaSpecies getSpecies(Double bugsCode){
        TaxonomicOrder taxonomicOrder = orderConverter.convertToSeadType(bugsCode);
        if(taxonomicOrder != null){
            return taxonomicOrder.getSpecies();
        }
        return null;
    }

    private TextDistribution getOrCreate(Distrib bugsData, TaxaSpecies species, Biblio reference){
        TextDistribution foundDistribution = distributionRepository.findByDistributionAndSpeciesAndReference(bugsData.getData(), species, reference);
        if(foundDistribution == null){
            return new TextDistributionBuilder(bugsData, species, reference).create();
        }
        return foundDistribution;
    }

    private static class TextDistributionBuilder {
        private TextDistribution seadValue;
        private Distrib bugsData;
        private TaxaSpecies species;
        private Biblio reference;

        public TextDistributionBuilder(Distrib bugsData, TaxaSpecies species, Biblio reference) {
            this.bugsData = bugsData;
            this.species = species;
            this.reference = reference;
        }

        TextDistribution create(){
            seadValue = new TextDistribution();
            setDistribution();
            setSpecies();
            setReference();
            return seadValue;
        }

        private void setDistribution() {
            if(bugsData.getData() == null || bugsData.getData().isEmpty()){
                seadValue.addError("Cannot add empty distribution text");
            }
            seadValue.setDistribution(bugsData.getData());
        }

        private void setSpecies() {
            if(species == null){
                seadValue.addError("No species found for code: " + bugsData.getCode());
            }
            seadValue.setSpecies(species);
        }

        private void setReference() {
            if(reference == null){
                seadValue.addError("No reference found for ref: " + bugsData.getRef());
            }
            seadValue.setReference(reference);
        }
    }
}
