package se.sead.taxanotes;

import se.sead.bugsimport.taxanotes.bugsmodel.TaxoNotes;

import java.util.Arrays;
import java.util.List;

class ExpectedBugsData {

    static final List<TaxoNotes> EXPECTED_DATA =
            Arrays.asList(
                    create(null, "Ref 2", "No species defined"),
                    create(1d, "Ref 1", "Note exists"),
                    create(2d, "Ref 1", "Note to be inserted"),
                    create(3d, "Ref 3", "Ref does not exist"),
                    create(4d, "Ref 2", "Species does not exist"),
                    create(5d, "Ref 2", null),
                    create(6d, null, "No reference")
            );

    private static TaxoNotes create(Double code, String ref, String data){
        TaxoNotes notes = new TaxoNotes();
        notes.setCode(code);
        notes.setReference(ref);
        notes.setData(data);
        return notes;
    }

}
