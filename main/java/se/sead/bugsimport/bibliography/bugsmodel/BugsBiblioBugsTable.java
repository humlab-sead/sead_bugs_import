package se.sead.bugsimport.bibliography.bugsmodel;

import com.healthmarketscience.jackcess.Row;
import se.sead.bugs.BugsTable;

public class BugsBiblioBugsTable extends BugsTable<BugsBiblio> {

    public static final String BUGS_TABLE_NAME = "TBiblio";

    public BugsBiblioBugsTable() {
        super(BUGS_TABLE_NAME);
    }

    @Override
    public BugsBiblio createItem(Row accessRow) {
        BugsBiblio reference = new BugsBiblio();
        reference.setReference(accessRow.getString("REFERENCE"));
        reference.setAuthor(accessRow.getString("AUTHOR"));
        reference.setTitle(accessRow.getString("TITLE"));
        reference.setNotes(accessRow.getString("Notes"));
        return reference;
    }
}
