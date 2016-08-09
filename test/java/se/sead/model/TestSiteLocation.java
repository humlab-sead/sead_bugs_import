package se.sead.model;

import se.sead.bugsimport.country.seadmodel.Location;
import se.sead.bugsimport.site.seadmodel.SeadSite;
import se.sead.bugsimport.site.seadmodel.SiteLocation;

public class TestSiteLocation extends SiteLocation {

    private TestSiteLocation(Integer id){
        super.setId(id);
    }

    public static SiteLocation create(Integer id, Location location){
        return create(id, location, null);
    }

    public static SiteLocation create(Integer id, Location location, SeadSite site){
        SiteLocation siteLocation = new TestSiteLocation(id);
        siteLocation.setLocation(location);
        siteLocation.setSite(site);
        return siteLocation;
    }

    public static SiteLocation create(Integer id, SeadSite site){
        return create(id, null, site);
    }
}
