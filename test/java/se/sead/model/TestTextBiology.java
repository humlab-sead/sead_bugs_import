package se.sead.model;

import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.bugsimport.speciesbiology.seadmodel.TextBiology;
import se.sead.bugsimport.bibliography.seadmodel.Biblio;

public class TestTextBiology extends TextBiology {

    private TestTextBiology(Integer id){
        super.setId(id);
    }

    public static TextBiology create(Integer id, TaxaSpecies species, Biblio reference, String data){
        TextBiology biology = new TestTextBiology(id);
        biology.setSpecies(species);
        biology.setReference(reference);
        biology.setText(data);
        return biology;
    }
}
