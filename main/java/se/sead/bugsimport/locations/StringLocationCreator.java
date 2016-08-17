package se.sead.bugsimport.locations;

import se.sead.bugsimport.country.LocationCreator;
import se.sead.bugsimport.country.seadmodel.LocationType;

class StringLocationCreator extends LocationCreator {
    private String locationName;
    private String bugsSiteCode;

    StringLocationCreator(LocationType type, String locationName, String siteCode){
        super(type);
        this.locationName = locationName;
        this.bugsSiteCode = siteCode;
    }

    @Override
    protected String getBugsLocationName() {
        return locationName;
    }

    @Override
    protected String getErrorForEmptyName() {
        return "Location name cannot be empty for site: " + bugsSiteCode;
    }
}
