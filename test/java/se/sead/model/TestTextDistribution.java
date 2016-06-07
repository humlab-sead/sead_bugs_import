package se.sead.model;

import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.bugsimport.speciesdistribution.seadmodel.TextDistribution;
import se.sead.sead.model.Biblio;

public class TestTextDistribution extends TextDistribution {

    private TestTextDistribution(Integer id){
        super.setId(id);
    }

    public static TextDistribution create(Integer id, TaxaSpecies species, Biblio reference, String textDistribution){
        TextDistribution distribution = new TestTextDistribution(id);
        distribution.setSpecies(species);
        distribution.setReference(reference);
        distribution.setDistribution(textDistribution);
        return distribution;
    }
}
