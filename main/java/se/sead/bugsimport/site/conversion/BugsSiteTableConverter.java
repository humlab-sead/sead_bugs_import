package se.sead.bugsimport.site.conversion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.BugsTableRowConverter;
import se.sead.bugsimport.country.seadmodel.Location;
import se.sead.bugsimport.site.bugsmodel.BugsSite;
import se.sead.bugsimport.site.locations.LocationHandler;
import se.sead.bugsimport.site.seadmodel.SeadSite;
import se.sead.repositories.LocationRepository;
import se.sead.repositories.LocationTypeRepository;
import se.sead.repositories.SiteRepository;

import java.util.Collections;
import java.util.List;

@Component
public class BugsSiteTableConverter implements BugsTableRowConverter<BugsSite, SeadSite>{

    @Autowired
    private LocationTypeRepository locationTypeRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private SiteRepository siteRepository;
    @Value("${allow.create.country:true}")
    private Boolean canCreateCountry;
    @Value("${allow.site.updates:false}")
    private Boolean allowSiteUpdates;


    @Override
    public SeadSite convertForDataRow(BugsSite bugsData) {
        LocationHandler locationHandler = getLocations(bugsData);
        return getOrCreate(bugsData, locationHandler);
    }

    private LocationHandler getLocations(BugsSite bugsData) {
        return new LocationHandler(locationRepository, locationTypeRepository, bugsData, canCreateCountry);
    }

    private SeadSite getOrCreate(BugsSite bugsSite, LocationHandler locationHandler){
        if(!locationHandler.countryExists()){
            return createMissingCountryLocationSite(bugsSite);
        }
        List<SeadSite> possibleSites = getPossibleExistingSites(bugsSite, locationHandler);
        if(possibleSites == null || possibleSites.isEmpty()){
            return createSite(bugsSite, locationHandler.getLocations());
        } else if(possibleSites.size() == 1){
            return update(bugsSite, possibleSites.get(0), locationHandler);
        } else {
            return createNonUniqueSite(bugsSite);
        }
    }

    private SeadSite createMissingCountryLocationSite(BugsSite bugsSite){
        return new SeadSiteCreator(
                bugsSite,
                Collections.EMPTY_LIST,
                "No country exists for site " + bugsSite.getCode()
        ).create();
    }

    private List<SeadSite> getPossibleExistingSites(BugsSite bugsSite, LocationHandler locationHandler) {
        List<SeadSite> possibleSites;
        if(!locationHandler.anyCreatedLocations()){
            possibleSites = siteRepository.findByNameAndLocations(bugsSite.getName(), locationHandler.getLocations());
        } else {
            possibleSites = siteRepository.findAllByName(bugsSite.getName());
        }
        return possibleSites;
    }

    private SeadSite createSite(BugsSite bugsSite, List<Location> locations) {
        return new SeadSiteCreator(bugsSite, locations).create();
    }

    private SeadSite update(BugsSite bugsData, SeadSite seadData, LocationHandler locationHandler){
        SiteUpdater updater = new SiteUpdater(bugsData, seadData, locationHandler);
        if(updater.needUpdates()) {
            if(allowSiteUpdates) {
                updater.doUpdates();
            } else {
                seadData.addError("Bugs data is updated but updates are disallowed.");
            }
        }

        return seadData;
    }

    private SeadSite createNonUniqueSite(BugsSite bugsSite){
        return new SeadSiteCreator(
                bugsSite,
                Collections.EMPTY_LIST,
                "More than one site found by name and location for code: " + bugsSite.getCode()
        ).create();
    }

}
