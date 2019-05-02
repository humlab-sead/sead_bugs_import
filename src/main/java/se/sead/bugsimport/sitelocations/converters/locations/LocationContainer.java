package se.sead.bugsimport.sitelocations.converters.locations;

import se.sead.bugsimport.site.seadmodel.SeadSite;
import se.sead.bugsimport.sitelocations.seadmodel.SiteLocation;

import java.util.ArrayList;
import java.util.List;

public class LocationContainer {
    private List<SiteLocation> locations;
    private SeadSite site;

    LocationContainer(SeadSite site){
        this.site = site;
        locations = new ArrayList<>();
    }

    void add(SiteLocation location){
        if(location != null) {
            location.setSite(site);
            locations.add(location);
        }
    }

    public boolean anyErrors(){
        return locations.stream().anyMatch(location ->  !location.isErrorFree());
    }

    public List<SiteLocation> getLocations(){
        return locations;
    }
}
