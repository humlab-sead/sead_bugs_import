package se.sead.bugsimport.sitelocations.converters;

import se.sead.bugsimport.locations.seadmodel.Location;
import se.sead.bugsimport.site.seadmodel.SeadSite;
import se.sead.bugsimport.sitelocations.seadmodel.SiteLocation;

import java.util.Arrays;
import java.util.List;

public class SiteLocationCreator {

    private Location location;
    private String errorMessage;
    private SeadSite site;

    public SiteLocationCreator(Location location){
        this(location, null);
    }

    SiteLocationCreator(Location location, String errorMessage){
        this.location = location;
        this.errorMessage = errorMessage;
    }

    public SiteLocationCreator(SeadSite site, Location location){
        this(site, location, null);
    }

    public SiteLocationCreator(SeadSite site, Location location, String errorMessage){
        this(location, errorMessage);
        this.site = site;
    }

    public SiteLocation create(){
        SiteLocation siteLocation = new SiteLocation();
        siteLocation.setLocation(location);
        siteLocation.setSite(site);
        addErrors(siteLocation);
        return siteLocation;
    }

    private void addErrors(SiteLocation siteLocation){
        addInheritedSiteErrors(siteLocation);
        addInheritedLocationErrors(siteLocation);
        addSiteLocationError(siteLocation);
    }

    private void addInheritedSiteErrors(SiteLocation siteLocation) {
        if(site != null && !site.isErrorFree()){
            addErrors(siteLocation, site.getErrors());
        }
    }

    private void addErrors(SiteLocation siteLocation, List<String> errors){
        for (String errorMessage :
                errors) {
            siteLocation.addError(errorMessage);
        }
    }

    private void addInheritedLocationErrors(SiteLocation siteLocation){
        if(location != null && !location.isErrorFree()){
            addErrors(siteLocation, location.getErrors());
        }
    }

    private void addSiteLocationError(SiteLocation siteLocation){
        if(errorMessage != null){
            siteLocation.addError(errorMessage);
        }
    }
}
