package se.sead.bugsimport.sitelocations.converters.locations.search;

import se.sead.bugsimport.site.helper.SiteFromCodeDisallowDeletedSite;
import se.sead.bugsimport.site.seadmodel.SeadSite;
import se.sead.bugsimport.sitelocations.bugsmodel.BugsSiteLocation;
import se.sead.bugsimport.sitelocations.converters.SiteLocationBugsTableRowConverter;
import se.sead.bugsimport.sitelocations.converters.SiteLocationCreator;
import se.sead.bugsimport.sitelocations.converters.locations.NameAndTypeFilter;
import se.sead.bugsimport.sitelocations.seadmodel.SiteLocation;
import se.sead.bugsimport.tracing.SeadDataFromTraceHelper;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.repositories.SiteLocationRepository;

import java.util.List;

public class BySiteSearchStrategy implements SearchStrategies {

    private SiteLocationRepository siteLocationRepository;
    private NameAndTypeFilter filter;
    private SiteFromCodeDisallowDeletedSite siteTraceHelper;

    public BySiteSearchStrategy(
            SiteLocationRepository siteLocationRepository,
            SiteFromCodeDisallowDeletedSite siteTraceHelper,
            NameAndTypeFilter filter) {
        this.siteLocationRepository = siteLocationRepository;
        this.siteTraceHelper = siteTraceHelper;
        this.filter = filter;
    }

    @Override
    public SiteLocation getSiteLocation(SeadSite site, BugsSiteLocation bugsSiteLocation) {
        List<SiteLocation> siteLocations = siteLocationRepository.findBySite(site);
        if (locationsEditedAfterInitialSiteImport(bugsSiteLocation.getSiteCode(), siteLocations)) {
            return createSiteLocationsEditedAfterInitialImportError(site);
        }
        return filter.filterForNameAndType(bugsSiteLocation, siteLocations);
    }

    private boolean locationsEditedAfterInitialSiteImport(String siteCode, List<SiteLocation> foundLocations) {
        BugsTrace latestSiteTrace = siteTraceHelper.getLatest(siteCode);
        return foundLocations.stream()
                .anyMatch(siteLocation ->
                        SeadDataFromTraceHelper.seadDataExistsAndHasBeenEditedSinceImport(siteLocation, latestSiteTrace));
    }

    private SiteLocation createSiteLocationsEditedAfterInitialImportError(SeadSite site){
        return new SiteLocationCreator(site,
                SiteLocationBugsTableRowConverter.ERROR_LOCATION,
                "Sead site has been updated since last bugs import").create();
    }
}
