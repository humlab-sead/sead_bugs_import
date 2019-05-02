package se.sead.bugsimport.rdb.bugsmodel;

import com.healthmarketscience.jackcess.Row;
import se.sead.bugs.BugsTable;

public class RDBBugsTable extends BugsTable<BugsRDB> {

    public static final String TABLE_NAME = "TRDB";

    public RDBBugsTable() {
        super(TABLE_NAME);
    }

    @Override
    public BugsRDB createItem(Row accessRow) {
        BugsRDB rdb = new BugsRDB();
        rdb.setCode(accessRow.getDouble("CODE"));
        rdb.setCountryCode(accessRow.getString("CountryCode"));
        rdb.setRdbCode(accessRow.getInt("RDBCode"));
        return rdb;
    }
}
