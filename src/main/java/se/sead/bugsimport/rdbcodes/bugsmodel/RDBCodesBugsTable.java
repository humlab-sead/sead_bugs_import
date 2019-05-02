package se.sead.bugsimport.rdbcodes.bugsmodel;

import com.healthmarketscience.jackcess.Row;
import se.sead.bugs.BugsTable;

public class RDBCodesBugsTable extends BugsTable<BugsRDBCodes> {

    public static final String TABLE_NAME = "TRDBCodes";

    public RDBCodesBugsTable(){
        super(TABLE_NAME);
    }

    @Override
    public BugsRDBCodes createItem(Row accessRow) {
        BugsRDBCodes code = new BugsRDBCodes();
        code.setRdbSystemCode(accessRow.getInt("RDBSystemCode"));
        code.setCategory(accessRow.getString("Category"));
        code.setRdbDefinition(accessRow.getString("RDBDefinition"));
        code.setRdbCode(accessRow.getInt("RDBCode"));
        return code;
    }
}
