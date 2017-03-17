package se.sead.model;

import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.bugsimport.taxanotes.seadmodel.TaxonomicNotes;
import se.sead.bugsimport.bibliography.seadmodel.Biblio;

public class TestTaxonomicNotes extends TaxonomicNotes {

    private TestTaxonomicNotes(Integer id){
        super.setId(id);
    }

    public static TaxonomicNotes create(Integer id, Biblio reference, TaxaSpecies species, String notes){
        TaxonomicNotes note = new TestTaxonomicNotes(id);
        note.setNotes(notes);
        note.setReference(reference);
        note.setSpecies(species);
        return note;
    }
}
