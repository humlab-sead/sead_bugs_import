package se.sead.bugsimport.taxanotes.bugsmodel;

import com.healthmarketscience.jackcess.Row;
import se.sead.bugs.BugsTable;

public class TaxoNotesBugsTable extends BugsTable<TaxoNotes> {

    static final String TABLE_NAME = "TTaxoNotes";

    public TaxoNotesBugsTable(){
        super(TABLE_NAME);
    }

    @Override
    public TaxoNotes createItem(Row accessRow) {
        TaxoNotes notes = new TaxoNotes();
        notes.setData(accessRow.getString("Data"));
        notes.setReference(accessRow.getString("Ref"));
        notes.setCode(accessRow.getDouble("CODE"));
        return notes;
    }
}
