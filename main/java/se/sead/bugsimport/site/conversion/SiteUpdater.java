package se.sead.bugsimport.site.conversion;

import se.sead.utils.BigDecimalDefinition;
import se.sead.bugsimport.site.bugsmodel.BugsSite;
import se.sead.bugsimport.site.seadmodel.SeadSite;

import java.util.Objects;

class SiteUpdater {

    private SeadSite seadData;
    private SeadSite createdNewVersion;

    SiteUpdater(BugsSite bugsData, SeadSite seadData) {
        this.seadData = seadData;
        createdNewVersion = new SeadSiteCreator(bugsData).create();
    }

    boolean needUpdates(){
        return !createdNewVersion.equals(createCopyWithoutIdAndLocations(seadData));
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
        if(!BigDecimalDefinition.equalBigDecimalNumericValues(seadData.getLatitude(), createdNewVersion.getLatitude())
                && createdNewVersion.getLatitude() != null){
            seadData.setLatitude(createdNewVersion.getLatitude());
            updated = true;
        }
        if(!BigDecimalDefinition.equalBigDecimalNumericValues(seadData.getLongitude(), createdNewVersion.getLongitude())
                && createdNewVersion.getLongitude() != null){
            seadData.setLongitude(createdNewVersion.getLongitude());
            updated = true;
        }
        if(!BigDecimalDefinition.equalBigDecimalNumericValues(seadData.getAltitude(), createdNewVersion.getAltitude())
                && createdNewVersion.getAltitude() != null){
            seadData.setAltitude(createdNewVersion.getAltitude());
            updated = true;
        }
        if(!Objects.equals(seadData.getName(), createdNewVersion.getName())){
            seadData.setName(createdNewVersion.getName());
            updated = true;
        }
        seadData.setUpdated(updated);
    }

}
