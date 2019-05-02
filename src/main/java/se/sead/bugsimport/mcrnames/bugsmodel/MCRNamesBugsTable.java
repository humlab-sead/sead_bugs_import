package se.sead.bugsimport.mcrnames.bugsmodel;

import com.healthmarketscience.jackcess.Row;
import se.sead.bugs.BugsTable;

public class MCRNamesBugsTable extends BugsTable<BugsMCRNames> {

    public static final String TABLE_NAME = "TMCRNames";

    public MCRNamesBugsTable(){
        super(TABLE_NAME);
    }

    @Override
    public BugsMCRNames createItem(Row accessRow) {
        BugsMCRNames names = new BugsMCRNames();
        names.setCode(accessRow.getDouble("CODE"));
        names.setCompareStatus(accessRow.getString("CompareStatus"));
        names.setMcrName(accessRow.getString("MCRName"));
        names.setMcrNameTrim(accessRow.getString("MCRNameTrim"));
        names.setMcrNumber(accessRow.getShort("MCRNumber"));
        names.setTempCode(accessRow.getDouble("tempCODE"));
        return names;
    }
}
