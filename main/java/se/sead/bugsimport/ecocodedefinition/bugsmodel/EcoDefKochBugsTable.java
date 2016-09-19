package se.sead.bugsimport.ecocodedefinition.bugsmodel;

import com.healthmarketscience.jackcess.Row;
import se.sead.bugs.BugsTable;

public class EcoDefKochBugsTable extends BugsTable<EcoDefKoch> {

    public static final String TABLE_NAME = "TEcoDefKoch";

    public EcoDefKochBugsTable(){
        super(TABLE_NAME);
    }

    @Override
    public EcoDefKoch createItem(Row accessRow) {
        EcoDefKoch def = new EcoDefKoch();
        def.setBugsKochCode(accessRow.getString("BugsKochCode"));
        def.setKochCode(accessRow.getString("KochCode"));
        def.setFullName(accessRow.getString("FullName"));
        def.setKochGroup(accessRow.getString("KochGroup"));
        def.setDescription(accessRow.getString("Description"));
        def.setNotes(accessRow.getString("Notes"));
        return def;
    }
}
