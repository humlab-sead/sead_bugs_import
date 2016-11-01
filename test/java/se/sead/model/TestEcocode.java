package se.sead.model;

import se.sead.bugsimport.ecocodedefinition.seadmodel.EcocodeDefinition;
import se.sead.bugsimport.ecocodes.seadmodel.Ecocode;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;

public class TestEcocode extends Ecocode {

    private TestEcocode(Integer id){
        setId(id);
    }

    public static Ecocode create(
            Integer id,
            TaxaSpecies species,
            EcocodeDefinition definition
    ){
        Ecocode ecocode = new TestEcocode(id);
        ecocode.setEcocodeDefinition(definition);
        ecocode.setSpecies(species);
        return ecocode;
    }
}
