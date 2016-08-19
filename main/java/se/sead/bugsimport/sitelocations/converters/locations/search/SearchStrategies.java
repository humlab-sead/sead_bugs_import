package se.sead.bugsimport.sitelocations.converters.locations.search;

import se.sead.bugsimport.site.seadmodel.SeadSite;
import se.sead.bugsimport.sitelocations.bugsmodel.BugsSiteLocation;
import se.sead.bugsimport.sitelocations.seadmodel.SiteLocation;

public interface SearchStrategies {
    SiteLocation getSiteLocation(SeadSite site, BugsSiteLocation bugsSiteLocation);
}
