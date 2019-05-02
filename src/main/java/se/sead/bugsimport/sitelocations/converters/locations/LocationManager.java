package se.sead.bugsimport.sitelocations.converters.locations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.site.seadmodel.SeadSite;
import se.sead.bugsimport.sitelocations.bugsmodel.BugsSiteLocation;

@Component
public class LocationManager {

    @Autowired
    private SiteLocationCountrySiteLocationManager countryLocationManager;
    @Autowired
    private SiteLocationRegionLocationManager regionLocationManager;

    public LocationContainer getLocations(SeadSite site, BugsSiteLocation bugsSiteLocations){
        LocationContainer container = new LocationContainer(site);
        container.add(countryLocationManager.getOrCreate(site, bugsSiteLocations));
        container.add(regionLocationManager.getOrCreate(site, bugsSiteLocations));
        return container;
    }

}
