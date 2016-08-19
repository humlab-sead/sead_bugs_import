package se.sead.bugsimport.sitelocations.converters.locations;

import se.sead.bugsimport.locations.seadmodel.Location;
import se.sead.bugsimport.site.helper.SiteFromCodeDisallowDeletedSite;
import se.sead.bugsimport.site.seadmodel.SeadSite;
import se.sead.bugsimport.sitelocations.bugsmodel.BugsSiteLocation;
import se.sead.bugsimport.sitelocations.converters.SiteLocationCreator;
import se.sead.bugsimport.sitelocations.converters.SiteLocationTraceHelper;
import se.sead.bugsimport.sitelocations.converters.locations.search.BySiteSearchStrategy;
import se.sead.bugsimport.sitelocations.converters.locations.search.SearchStrategies;
import se.sead.bugsimport.sitelocations.converters.locations.search.TraceSearchStrategy;
import se.sead.bugsimport.sitelocations.seadmodel.SiteLocation;
import se.sead.repositories.SiteLocationRepository;

import java.util.Arrays;
import java.util.List;

public abstract class SiteLocationManagerAccessor {

    private List<SearchStrategies> searchStrategies;

    protected SiteLocationManagerAccessor(
            SiteLocationRepository siteLocationRepository,
            SiteLocationTraceHelper traceHelper,
            SiteFromCodeDisallowDeletedSite siteTraceHelper,
            NameAndTypeFilter nameAndTypeFilter){
        searchStrategies = Arrays.asList(
            new TraceSearchStrategy(traceHelper),
            new BySiteSearchStrategy(siteLocationRepository, siteTraceHelper, nameAndTypeFilter)
        );
    }

    public SiteLocation getOrCreate(SeadSite site, BugsSiteLocation bugsSiteLocation){
        SiteLocation foundSiteLocation = null;
        for (int i = 0; i < searchStrategies.size() && foundSiteLocation == null; i++) {
            foundSiteLocation = searchStrategies.get(i).getSiteLocation(site, bugsSiteLocation);
        }
        if(foundSiteLocation == null){
            return getByLocationName(site, bugsSiteLocation);
        }
        return foundSiteLocation;
    }

    private SiteLocation getByLocationName(SeadSite site, BugsSiteLocation bugsSiteLocations) {
        Location storedCountry = searchDatabase(bugsSiteLocations);
        if(storedCountry == null){
            return new SiteLocationCreator(site, create(bugsSiteLocations)).create();
        } else {
            return new SiteLocationCreator(site, storedCountry).create();
        }
    }

    protected abstract Location searchDatabase(BugsSiteLocation bugsSiteLocation);

    protected abstract Location create(BugsSiteLocation bugsSiteLocation);

}
