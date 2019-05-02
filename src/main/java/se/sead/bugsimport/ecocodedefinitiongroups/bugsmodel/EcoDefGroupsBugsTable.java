package se.sead.bugsimport.ecocodedefinitiongroups.bugsmodel;

import com.healthmarketscience.jackcess.Row;
import se.sead.bugs.BugsTable;

public class EcoDefGroupsBugsTable extends BugsTable<EcoDefGroups> {

    public static final String TABLE_NAME = "TEcoDefGroups";

    public EcoDefGroupsBugsTable(){
        super(TABLE_NAME);
    }

    @Override
    public EcoDefGroups createItem(Row accessRow) {
        EcoDefGroups group = new EcoDefGroups();
        group.setEcoGroupCode(accessRow.getString("EcoGroupCode"));
        group.setEcoName(accessRow.getString("EcoName"));
        return group;
    }
}
