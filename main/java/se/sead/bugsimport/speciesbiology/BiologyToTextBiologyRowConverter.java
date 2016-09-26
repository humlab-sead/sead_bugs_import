package se.sead.bugsimport.speciesbiology;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.speciesbiology.bugsmodel.Biology;
import se.sead.bugsimport.BugsTableRowConverter;
import se.sead.sead.model.Biblio;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.bugsimport.speciesbiology.seadmodel.TextBiology;
import se.sead.repositories.BiblioDataRepository;
import se.sead.repositories.TaxonomicOrderRepository;
import se.sead.repositories.TextBiologyDataRepository;
import se.sead.utils.BigDecimalDefinition;

@Component
public class BiologyToTextBiologyRowConverter implements BugsTableRowConverter<Biology, TextBiology> {

    @Autowired
    private TaxonomicOrderRepository taxonomicOrderRepository;
    @Autowired
    private BiblioDataRepository bibliographyDataRepository;
    @Autowired
    private TextBiologyDataRepository biologyDataRepository;

    @Override
    public TextBiology convertForDataRow(Biology bugsData) {
        TaxaSpecies species = getSpecies(bugsData.getCode());
        Biblio reference = getReference(bugsData.getRef());
        return getOrCreate(bugsData, species, reference);
    }

    private TaxaSpecies getSpecies(Double code) {
        TaxaSpecies species = taxonomicOrderRepository
                .findBugsSpeciesByCode(BigDecimalDefinition.convertToSeadCode(code));
        return species;
    }

    private Biblio getReference(String ref) {
        return bibliographyDataRepository.getByBugsReferenceIgnoreCase(ref);
    }

    private TextBiology getOrCreate(Biology bugsData, TaxaSpecies species, Biblio reference) {
        TextBiology existingBiologyEntry = biologyDataRepository.findBySpeciesAndTextIgnoreCaseAndReference(species, bugsData.getData(), reference);
        if(existingBiologyEntry == null){
            return create(bugsData, species, reference);
        }
        return existingBiologyEntry;
    }

    private TextBiology create(Biology bugsData, TaxaSpecies species, Biblio reference){
        TextBiologyCreator creator = new TextBiologyCreator(bugsData, species, reference);
        return creator.create();
    }

    private static class TextBiologyCreator {
        private TextBiology createdBiology;
        private Biology bugsData;
        private TaxaSpecies species;
        private Biblio reference;

        public TextBiologyCreator(Biology bugsData, TaxaSpecies species, Biblio reference) {
            this.bugsData = bugsData;
            this.species = species;
            this.reference = reference;
        }

        TextBiology create(){
            createdBiology = new TextBiology();
            setText();
            setSpecies();
            setReference();
            return createdBiology;
        }

        private void setText() {
            String bugsText = bugsData.getData();
            if(bugsText != null && !bugsText.isEmpty()){
                createdBiology.setText(bugsText);
            } else {
                createdBiology.addError("No text provided for code: " + getBugsCode());
            }
        }

        private Double getBugsCode(){
            return bugsData.getCode();
        }

        private void setSpecies(){
            if(species != null){
                createdBiology.setSpecies(species);
            } else {
                createdBiology.addError("No species found for code: " + getBugsCode());
            }
        }

        private void setReference(){
            if(reference != null){
                createdBiology.setReference(reference);
            } else {
                createdBiology.addError("No reference found for reference: " + bugsData.getRef());
            }
        }
    }
}
