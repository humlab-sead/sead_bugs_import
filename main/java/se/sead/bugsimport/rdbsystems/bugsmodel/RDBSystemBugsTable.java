package se.sead.bugsimport.rdbsystems.bugsmodel;

import com.healthmarketscience.jackcess.Row;
import se.sead.bugs.BugsTable;

public class RDBSystemBugsTable extends BugsTable<BugsRDBSystem> {

    public static final String TABLE_NAME = "TRDBSystems";

    public RDBSystemBugsTable(){
        super(TABLE_NAME);
    }

    @Override
    public BugsRDBSystem createItem(Row accessRow) {
        BugsRDBSystem rdbSystem = new BugsRDBSystem();
        rdbSystem.setRdbSystemCode(accessRow.getInt("RDBSystemCode"));
        rdbSystem.setRdbSystem(accessRow.getString("RDBSystem"));
        rdbSystem.setRdbVersion(accessRow.getString("RDBVersion"));
        rdbSystem.setRdbSystemDate(accessRow.getInt("RDBSystemDate"));
        rdbSystem.setRdbFirstPublished(accessRow.getShort("RDBFirstPublished"));
        rdbSystem.setRef(accessRow.getString("Ref"));
        rdbSystem.setCountryCode(accessRow.getString("CountryCode"));
        return rdbSystem;
    }
}
