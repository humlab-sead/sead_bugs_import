package se.sead.bugsimport.site.conversion;

import se.sead.BigDecimalDefinition;
import se.sead.bugsimport.site.bugsmodel.BugsSite;
import se.sead.bugsimport.site.locations.LocationHandler;
import se.sead.bugsimport.site.seadmodel.SeadSite;

import java.util.Collections;
import java.util.Objects;

class SiteUpdater {

    private SeadSite seadData;
    private SeadSite createdNewVersion;
    private SiteLocationUpdater siteLocationUpdater;

    SiteUpdater(BugsSite bugsData, SeadSite seadData, LocationHandler locationHandler) {
        this.seadData = seadData;
        this.siteLocationUpdater = new SiteLocationUpdater(locationHandler, seadData);
        createdNewVersion = new SeadSiteCreator(bugsData, Collections.EMPTY_LIST).create();
    }

    boolean needUpdates(){
        return siteLocationUpdater.differingLocations() ||
                !createdNewVersion.equals(createCopyWithoutIdAndLocations(seadData));
    }

    private SeadSite createCopyWithoutIdAndLocations(SeadSite site){
        SeadSite copy = new SeadSite();
        copy.setAltitude(site.getAltitude());
        copy.setLongitude(site.getLongitude());
        copy.setLatitude(site.getLatitude());
        copy.setNationalSiteIdentifier(site.getNationalSiteIdentifier());
        copy.setName(site.getName());
        copy.setDescription(site.getDescription());
        return copy;
    }

    void doUpdates(){
        boolean updated = false;
        if(!Objects.equals(seadData.getNationalSiteIdentifier(), createdNewVersion.getNationalSiteIdentifier())){
            seadData.setNationalSiteIdentifier(createdNewVersion.getNationalSiteIdentifier());
            updated = true;
        }
        if(!Objects.equals(seadData.getDescription(), createdNewVersion.getDescription())){
            seadData.setDescription(createdNewVersion.getDescription());
            updated = true;
        }
        if(BigDecimalDefinition.equalBigDecimalNumericValues(seadData.getLatitude(), createdNewVersion.getLatitude())
                && createdNewVersion.getLatitude() != null){
            seadData.setLatitude(createdNewVersion.getLatitude());
            updated = true;
        }
        if(BigDecimalDefinition.equalBigDecimalNumericValues(seadData.getLongitude(), createdNewVersion.getLongitude())
                && createdNewVersion.getLongitude() != null){
            seadData.setLongitude(createdNewVersion.getLongitude());
            updated = true;
        }
        if(BigDecimalDefinition.equalBigDecimalNumericValues(seadData.getAltitude(), createdNewVersion.getAltitude())
                && createdNewVersion.getAltitude() != null){
            seadData.setAltitude(createdNewVersion.getAltitude());
            updated = true;
        }
        if(siteLocationUpdater.differingLocations()){
            siteLocationUpdater.update(seadData);
            updated = true;
        }
        seadData.setUpdated(updated);
    }

}
