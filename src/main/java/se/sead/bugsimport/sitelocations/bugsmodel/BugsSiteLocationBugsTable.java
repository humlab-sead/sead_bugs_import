package se.sead.bugsimport.sitelocations.bugsmodel;

import com.healthmarketscience.jackcess.Row;
import se.sead.bugs.BugsTable;

public class BugsSiteLocationBugsTable extends BugsTable<BugsSiteLocation> {

    static final String TABLE_NAME = "TSite";

    public BugsSiteLocationBugsTable(){
        super(TABLE_NAME);
    }

    @Override
    public BugsSiteLocation createItem(Row accessRow) {
        BugsSiteLocation siteLocation = new BugsSiteLocation();
        siteLocation.setSiteCode(accessRow.getString("SiteCODE"));
        siteLocation.setCountry(accessRow.getString("Country"));
        siteLocation.setRegion(accessRow.getString("Region"));
        return siteLocation;
    }
}
