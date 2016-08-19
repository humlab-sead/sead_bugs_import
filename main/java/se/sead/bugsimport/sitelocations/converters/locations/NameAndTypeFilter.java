package se.sead.bugsimport.sitelocations.converters.locations;

import se.sead.bugsimport.sitelocations.bugsmodel.BugsSiteLocation;
import se.sead.bugsimport.sitelocations.seadmodel.SiteLocation;

import java.util.List;

@FunctionalInterface
public interface NameAndTypeFilter {
    SiteLocation filterForNameAndType(BugsSiteLocation bugsSiteLocation, List<SiteLocation> foundDbLocations);
}
