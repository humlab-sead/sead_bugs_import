package se.sead.bibliography;

import se.sead.bugsimport.bibliography.bugsmodel.BugsBiblio;

import java.util.Arrays;
import java.util.List;

public class ExpectedBugsData {

    static final List<BugsBiblio> EXPECTED_DATA = Arrays.asList(
            create("Exists 2000", "Exists, A.B (2000)", "An existing bibliography non-edited", null),
            create("NonExists 2000", "Exists, Not (2000)", "A non-existing bibliography entry.", null),
            create("Exists 2001", "Exists, A.B (2001)", "A changed entry - do not handle", null)
    );

    private static BugsBiblio create(String reference, String author, String title, String notes){
        BugsBiblio ref = new BugsBiblio();
        ref.setReference(reference);
        ref.setAuthor(author);
        ref.setTitle(title);
        ref.setNotes(notes);
        return ref;
    }
}
