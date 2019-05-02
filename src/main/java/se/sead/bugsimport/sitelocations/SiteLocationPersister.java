package se.sead.bugsimport.sitelocations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.Persister;
import se.sead.bugsimport.locations.seadmodel.Location;
import se.sead.bugsimport.locations.seadmodel.LocationType;
import se.sead.bugsimport.sitelocations.bugsmodel.BugsSiteLocation;
import se.sead.bugsimport.sitelocations.converters.locations.SiteLocationCountrySiteLocationManager;
import se.sead.bugsimport.sitelocations.converters.locations.SiteLocationRegionLocationManager;
import se.sead.bugsimport.sitelocations.seadmodel.SiteLocation;
import se.sead.repositories.LocationTypeRepository;
import se.sead.repositories.SiteLocationRepository;

@Component
public class SiteLocationPersister extends Persister<BugsSiteLocation, SiteLocation>{

    @Autowired
    private SiteLocationRepository siteLocationRepository;
    private CacheManager cacheManager;

    @Autowired
    public SiteLocationPersister(
            SiteLocationCountrySiteLocationManager countrySiteLocationManager,
            SiteLocationRegionLocationManager regionLocationManager,
            LocationTypeRepository locationTypeRepository){
        cacheManager = new CacheManager(countrySiteLocationManager, regionLocationManager, locationTypeRepository);
    }

    @Override
    protected SiteLocation save(SiteLocation seadValue) {
        cacheManager.updateSiteLocations(seadValue);
        SiteLocation savedSiteLocation = siteLocationRepository.saveOrUpdate(seadValue);
        if(!seadValue.isMarkedForDeletion()) {
            cacheManager.updateCache(savedSiteLocation.getLocation());
        }
        return savedSiteLocation;
    }


    private static class CacheManager {
        private SiteLocationCountrySiteLocationManager countrySiteLocationManager;
        private SiteLocationRegionLocationManager regionLocationManager;
        private CountryLocationIdentifier countryLocationIdentifier;

        public CacheManager(
                SiteLocationCountrySiteLocationManager countrySiteLocationManager,
                SiteLocationRegionLocationManager regionLocationManager,
                LocationTypeRepository locationTypeRepository) {
            this.countrySiteLocationManager = countrySiteLocationManager;
            this.regionLocationManager = regionLocationManager;
            countryLocationIdentifier = new CountryLocationIdentifier(locationTypeRepository);
        }

        void updateSiteLocations(SiteLocation seadValue){
            seadValue.setLocation(updateLocation(seadValue.getLocation()));
        }

        private Location updateLocation(Location sourceLocation){
            if(countryLocationIdentifier.isCountry(sourceLocation)){
                return countrySiteLocationManager.getCachedValue(sourceLocation.getName());
            } else {
                return regionLocationManager.getCachedValue(sourceLocation.getName());
            }
        }

        void updateCache(Location location){
            if(countryLocationIdentifier.isCountry(location)){
                countrySiteLocationManager.updateCache(location.getName(), location);
            } else {
                regionLocationManager.updateCache(location.getName(), location);
            }
        }
    }

    private static class CountryLocationIdentifier {
        private LocationType countryType;
        CountryLocationIdentifier(LocationTypeRepository typeRepository){
            countryType = typeRepository.getCountryType();
        }
        boolean isCountry(Location location){
            return countryType.equals(location.getType());
        }

    }

}
