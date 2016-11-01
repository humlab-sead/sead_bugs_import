package se.sead.bugsimport.ecocodedefinition.bugs.bugsmodel;

import com.healthmarketscience.jackcess.Row;
import se.sead.bugs.BugsTable;

public class EcoDefBugsBugsTable extends BugsTable<EcoDefBugs> {

    public static final String TABLE_NAME = "TEcoDefBugs";

    public EcoDefBugsBugsTable(){
        super(TABLE_NAME);
    }

    @Override
    public EcoDefBugs createItem(Row accessRow) {
        EcoDefBugs ecoDefBugs = new EcoDefBugs();
        ecoDefBugs.setBugsEcoCODE(accessRow.getString("BugsEcoCODE"));
        ecoDefBugs.setEcoLabel(accessRow.getString("EcoLabel"));
        ecoDefBugs.setDefinition(accessRow.getString("Definition"));
        ecoDefBugs.setNotes(accessRow.getString("Notes"));
        ecoDefBugs.setSortOrder(accessRow.getShort("SortOrder"));
        return ecoDefBugs;
    }
}
