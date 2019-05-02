package se.sead.bugsimport.sitelocations.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.BugsTableRowConverter;
import se.sead.bugsimport.locations.seadmodel.Location;
import se.sead.bugsimport.site.helper.SiteFromCodeDisallowDeletedSite;
import se.sead.bugsimport.site.seadmodel.SeadSite;
import se.sead.bugsimport.sitelocations.bugsmodel.BugsSiteLocation;
import se.sead.bugsimport.sitelocations.converters.locations.LocationContainer;
import se.sead.bugsimport.sitelocations.converters.locations.LocationManager;
import se.sead.bugsimport.sitelocations.seadmodel.SiteLocation;
import se.sead.repositories.SiteLocationRepository;

import java.util.Arrays;
import java.util.List;

@Component
public class SiteLocationBugsTableRowConverter implements BugsTableRowConverter<BugsSiteLocation, SiteLocation> {

    public static final SeadSite ERROR_SITE = new SeadSite();
    public static final Location ERROR_LOCATION = new Location();

    @Autowired
    private SiteFromCodeDisallowDeletedSite siteFromBugsCodeHelper;
    @Autowired
    private LocationManager locationManager;
    @Autowired
    private SiteLocationRepository siteLocationRepository;

    @Override
    public List<SiteLocation> convertListForDataRow(BugsSiteLocation bugsData) {
        SeadSite seadSiteFromBugsCode = siteFromBugsCodeHelper.getSeadSiteFromBugsCode(bugsData.getSiteCode());
        if(seadSiteFromBugsCode == null){
            return createErrorLocations("Could not find imported site");
        } else if(!seadSiteFromBugsCode.isErrorFree()){
            return createErrorLocations(seadSiteFromBugsCode);
        }
        LocationContainer bugsLocations = locationManager.getLocations(seadSiteFromBugsCode, bugsData);
        if(bugsLocations.anyErrors()){
            return bugsLocations.getLocations();
        }
        List<SiteLocation> allStoredSiteLocations = siteLocationRepository.findBySite(seadSiteFromBugsCode);
        SiteLocationUpdater updater = new SiteLocationUpdater(bugsLocations.getLocations(), allStoredSiteLocations);
        return updater.getClearedItems();
    }

    private List<SiteLocation> createErrorLocations(String errorMessage){
        return Arrays.asList(
            new SiteLocationCreator(ERROR_SITE, ERROR_LOCATION, errorMessage).create()
        );
    }

    private List<SiteLocation> createErrorLocations(SeadSite seadSite){
        return Arrays.asList(new SiteLocationCreator(seadSite, ERROR_LOCATION).create());
    }

}
