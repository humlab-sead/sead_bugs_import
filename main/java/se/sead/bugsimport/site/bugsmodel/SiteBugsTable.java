package se.sead.bugsimport.site.bugsmodel;

import com.healthmarketscience.jackcess.Row;
import se.sead.bugs.BugsTable;

public class SiteBugsTable extends BugsTable<Site> {

    static final String TABLE_NAME = "TSite";

    public SiteBugsTable(){
        super(TABLE_NAME);
    }

    @Override
    public Site createItem(Row accessRow) {
        Site site = new Site();
        site.setCode(accessRow.getString("SiteCODE"));
        site.setName(accessRow.getString("SiteName"));
        site.setRegion(accessRow.getString("Region"));
        site.setCountry(accessRow.getString("Country"));
        site.setNgr(accessRow.getString("NGR"));
        site.setLatDD(accessRow.getFloat("LatDD"));
        site.setLongDD(accessRow.getFloat("LongDD"));
        site.setAlt(accessRow.getFloat("Alt"));
        site.setiDBy(accessRow.getString("IDBy"));
        site.setInterp(accessRow.getString("Interp"));
        site.setSpecimens(accessRow.getString("Specimens"));
        return site;
    }
}
