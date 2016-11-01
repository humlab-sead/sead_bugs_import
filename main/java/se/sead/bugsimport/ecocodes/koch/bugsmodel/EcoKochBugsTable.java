package se.sead.bugsimport.ecocodes.koch.bugsmodel;

import com.healthmarketscience.jackcess.Row;
import se.sead.bugs.BugsTable;

public class EcoKochBugsTable extends BugsTable<EcoKoch> {

    public static final String TABLE_NAME = "TEcoKoch";

    public EcoKochBugsTable(){
        super(TABLE_NAME);
    }

    @Override
    public EcoKoch createItem(Row accessRow) {
        EcoKoch ecoKoch = new EcoKoch();
        ecoKoch.setCODE(accessRow.getDouble("CODE"));
        ecoKoch.setBugsKochCode(accessRow.getString("BugsKochCode"));
        return ecoKoch;
    }
}
