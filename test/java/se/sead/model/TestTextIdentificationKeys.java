package se.sead.model;

import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.bugsimport.specieskeys.seadmodel.TextIdentificationKeys;
import se.sead.sead.model.Biblio;

public class TestTextIdentificationKeys extends TextIdentificationKeys {

    private TestTextIdentificationKeys(Integer id){
        super.setId(id);
    }

    public static TextIdentificationKeys create(Integer id, TaxaSpecies species, Biblio reference, String idenitificationKeys){
        TextIdentificationKeys key = new TestTextIdentificationKeys(id);
        key.setSpecies(species);
        key.setReference(reference);
        key.setKeys(idenitificationKeys);
        return key;
    }
}
