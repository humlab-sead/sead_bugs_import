package se.sead.bugsimport.ecocodes.bugs.bugsmodel;

import com.healthmarketscience.jackcess.Row;
import se.sead.bugs.BugsTable;

public class EcoBugsBugsTable extends BugsTable<EcoBugs> {

    public static final String TABLE_NAME = "TEcoBugs";

    public EcoBugsBugsTable(){
        super(TABLE_NAME);
    }

    @Override
    public EcoBugs createItem(Row accessRow) {
        EcoBugs ecoBugs = new EcoBugs();
        ecoBugs.setCode(accessRow.getDouble("CODE"));
        ecoBugs.setBugsEcoCode(accessRow.getString("BugsEcoCODE"));
        return ecoBugs;
    }
}
