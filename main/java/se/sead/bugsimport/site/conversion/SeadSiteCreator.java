package se.sead.bugsimport.site.conversion;

import org.springframework.util.StringUtils;
import se.sead.BigDecimalDefinition;
import se.sead.bugsimport.country.seadmodel.Location;
import se.sead.bugsimport.site.bugsmodel.BugsSite;
import se.sead.bugsimport.site.seadmodel.SeadSite;
import se.sead.bugsimport.site.seadmodel.SiteLocation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

class SeadSiteCreator {
    private List<Location> locations;
    private BugsSite bugsVersion;
    private SeadSite seadVersion;
    private String errorMessage;

    public SeadSiteCreator(BugsSite bugsVersion, List<Location> locations) {
        this.locations = locations;
        this.bugsVersion = bugsVersion;
    }

    public SeadSiteCreator(BugsSite bugsVersion, List<Location> locations, String errorMessage) {
        this.locations = locations;
        this.bugsVersion = bugsVersion;
        this.errorMessage = errorMessage;
    }

    SeadSite create(){
        seadVersion = new SeadSite();
        setName();
        setAltitude();
        setLatitude();
        setLongitude();
        setNationalIdentifier();
        setDescription();
        setLocations();
        setCustomErrorMessage();
        return seadVersion;
    }

    private void setName() {
        if(StringUtils.isEmpty(bugsVersion.getName())){
            seadVersion.addError("Site name cannot be empty");
        }
        seadVersion.setName(bugsVersion.getName());
    }

    private void setAltitude() {
        seadVersion.setAltitude(convertToBigDecimal(bugsVersion.getAlt()));
    }

    private static BigDecimal convertToBigDecimal(Float bugsVersion){
        return BigDecimalDefinition.convertToSeadContext(bugsVersion);
    }

    private void setLatitude() {
        seadVersion.setLatitude(convertToBigDecimal(bugsVersion.getLatDD()));
    }

    private void setLongitude() {
        seadVersion.setLongitude(convertToBigDecimal(bugsVersion.getLongDD()));
    }

    private void setNationalIdentifier() {
        seadVersion.setNationalSiteIdentifier(bugsVersion.getNgr());
    }

    private void setDescription() {
        seadVersion.setDescription(bugsVersion.getInterp());
    }

    private void setLocations() {
        if(!locations.isEmpty()){
            List<SiteLocation> siteLocations = convertToSiteLocations();
            seadVersion.setSiteLocations(siteLocations);
        }
    }

    private List<SiteLocation> convertToSiteLocations() {
        List<SiteLocation> siteLocations = new ArrayList<>(locations.size());
        for (Location location :
                locations) {
            SiteLocation siteLocation = new SiteLocation();
            siteLocation.setSite(seadVersion);
            siteLocation.setLocation(location);
            siteLocations.add(siteLocation);
        }
        return siteLocations;
    }

    private void setCustomErrorMessage(){
        if(!StringUtils.isEmpty(errorMessage)){
            seadVersion.addError(errorMessage);
        }
    }
}
