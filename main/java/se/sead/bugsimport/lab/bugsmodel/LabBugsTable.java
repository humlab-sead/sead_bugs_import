package se.sead.bugsimport.lab.bugsmodel;

import com.healthmarketscience.jackcess.Row;
import se.sead.bugs.BugsTable;

public class LabBugsTable extends BugsTable<BugsLab> {

    public static final String TABLE_NAME = "TLab";

    public LabBugsTable(){
        super(TABLE_NAME);
    }

    @Override
    public BugsLab createItem(Row accessRow) {
        BugsLab lab = new BugsLab();
        lab.setLabId(accessRow.getString("LabID"));
        lab.setLabName(accessRow.getString("Labname"));
        lab.setAddress(accessRow.getString("Address"));
        lab.setCountry(accessRow.getString("Country"));
        lab.setTelephone(accessRow.getString("Telephone"));
        return lab;
    }
}
