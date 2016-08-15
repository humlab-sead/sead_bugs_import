package se.sead.bugsimport.countsheets.bugsmodel;

import com.healthmarketscience.jackcess.Row;
import se.sead.bugs.BugsTable;

public class CountsheetBugsTable extends BugsTable<Countsheet> {

    static final String TABLE_NAME = "TCountsheet";

    public CountsheetBugsTable(){
        super(TABLE_NAME);
    }

    @Override
    public Countsheet createItem(Row accessRow) {
        Countsheet countsheet = new Countsheet();
        countsheet.setCode(accessRow.getString("CountsheetCODE"));
        countsheet.setSheetContext(accessRow.getString("SheetContext"));
        countsheet.setName(accessRow.getString("CountsheetName"));
        countsheet.setType(accessRow.getString("SheetType"));
        countsheet.setSiteCode(accessRow.getString("SiteCODE"));
        return countsheet;
    }
}
