package se.sead.bugsimport.sitereferences.bugsmodel;

import com.healthmarketscience.jackcess.Row;
import se.sead.bugs.BugsTable;

public class SiteRefBugsTable extends BugsTable<BugsSiteRef> {

    static final String TABLE_NAME = "TSiteRef";

    public SiteRefBugsTable(){
        super(TABLE_NAME);
    }

    @Override
    public BugsSiteRef createItem(Row accessRow) {
        BugsSiteRef siteRef = new BugsSiteRef();
        siteRef.setSiteCode(accessRow.getString("SiteCODE"));
        siteRef.setRef(accessRow.getString("Ref"));
        return siteRef;
    }
}
