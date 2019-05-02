package se.sead.bugsimport.specieskeys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.BugsTableRowConverter;
import se.sead.bugsimport.species.converters.TaxonomicOrderConverter;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.bugsimport.species.seadmodel.TaxonomicOrder;
import se.sead.bugsimport.specieskeys.bugsmodel.Keys;
import se.sead.bugsimport.specieskeys.seadmodel.TextIdentificationKeys;
import se.sead.repositories.BiblioDataRepository;
import se.sead.repositories.TextIdentificationKeysRepository;
import se.sead.bugsimport.bibliography.seadmodel.Biblio;

@Component
public class KeysTableRowConverter implements BugsTableRowConverter<Keys, TextIdentificationKeys> {

    @Autowired
    private TaxonomicOrderConverter orderConverter;
    @Autowired
    private BiblioDataRepository biblioRepository;
    @Autowired
    private TextIdentificationKeysRepository keysRepository;

    @Override
    public TextIdentificationKeys convertForDataRow(Keys bugsData) {
        TaxaSpecies species = getSpecies(bugsData.getCode());
        Biblio reference = getReference(bugsData.getRef());
        return getOrCreate(bugsData, species, reference);
    }

    private TaxaSpecies getSpecies(Double bugsCode) {
        TaxonomicOrder taxonomicOrder = orderConverter.convertToSeadType(bugsCode);
        if(taxonomicOrder != null){
            return taxonomicOrder.getSpecies();
        }
        return null;
    }

    private Biblio getReference(String ref) {
        if(ref == null){
            return null;
        }
        return biblioRepository.getByBugsReferenceIgnoreCase(ref);
    }

    private TextIdentificationKeys getOrCreate(Keys bugsData, TaxaSpecies species, Biblio reference) {
        TextIdentificationKeys previousItem = keysRepository.findByKeysAndSpeciesAndReference(bugsData.getData(), species, reference);
        if(previousItem == null) {
            return new TextIdentificationKeysBuilder(bugsData, species, reference).create();
        }
        return previousItem;
    }

    private static class TextIdentificationKeysBuilder {
        private TextIdentificationKeys seadData;
        private Keys bugsData;
        private TaxaSpecies species;
        private Biblio reference;

        TextIdentificationKeysBuilder(Keys bugsData, TaxaSpecies species, Biblio reference) {
            this.bugsData = bugsData;
            this.species = species;
            this.reference = reference;
        }

        TextIdentificationKeys create(){
            seadData = new TextIdentificationKeys();
            setSpecies();
            setReference();
            setKeys();
            return seadData;
        }

        private void setSpecies() {
            if(species == null){
                seadData.addError("No species found for code: " + bugsData.getCode());
            }
            seadData.setSpecies(species);
        }

        private void setReference() {
            if(reference == null){
                seadData.addError("No reference found for ref: " + bugsData.getRef());
            }
            seadData.setReference(reference);
        }

        private void setKeys() {
            if(bugsData.getData() == null || bugsData.getData().isEmpty()){
                seadData.addError("Keys without content not permitted");
            }
            seadData.setKeys(bugsData.getData());
        }
    }
}
