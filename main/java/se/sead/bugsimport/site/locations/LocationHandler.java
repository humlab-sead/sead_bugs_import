package se.sead.bugsimport.site.locations;

import se.sead.bugsimport.country.seadmodel.Location;
import se.sead.bugsimport.country.seadmodel.LocationType;
import se.sead.bugsimport.site.bugsmodel.BugsSite;
import se.sead.repositories.LocationRepository;
import se.sead.repositories.LocationTypeRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LocationHandler {

    private List<BaseLocationHandler> locationHandlers;
    private List<Location> gatheredLocations;
    private Boolean cachedCreatedLocations = null;

    private LocationType countryType;

    public LocationHandler(
            LocationRepository locationRepository,
            LocationTypeRepository locationTypeRepository,
            BugsSite bugsData,
            boolean canCreateNewCountryFromName) {
        locationHandlers = Arrays.asList(
                new RegionLocationHandler(locationTypeRepository, locationRepository, bugsData),
                new CountryLocationHandler(locationTypeRepository.getCountryType(), locationRepository, bugsData, canCreateNewCountryFromName)
        );
        countryType = locationTypeRepository.getCountryType();
    }

    public List<Location> getLocations() {
        if(gatheredLocations == null) {
            gatheredLocations = new ArrayList<>();
            for (BaseLocationHandler locationHandler :
                    locationHandlers) {
                gatheredLocations.addAll(locationHandler.getLocations());
            }
        }
        return gatheredLocations;
    }

    public boolean countryExists(){
        return getLocations().stream().anyMatch(location -> location.getType().equals(countryType));
    }

    public boolean anyCreatedLocations(){
        if(cachedCreatedLocations == null){
            cachedCreatedLocations =
                    locationHandlers.stream()
                    .anyMatch(locationHandler -> locationHandler.isNewLocationCreated());
        }
        return cachedCreatedLocations;
    }

    static abstract class BaseLocationHandler {

        protected BugsSite bugsData;
        private boolean newLocationCreated = false;

        protected BaseLocationHandler(BugsSite bugsData){
            this.bugsData = bugsData;
        }

        abstract List<Location> getLocations();

        protected Location createLocation(String locationName){
            newLocationCreated = true;
            return
                    new StringLocationCreator(
                            getLocationType(),
                            locationName,
                            bugsData.getCode()
                    ).create();
        }

        public boolean isNewLocationCreated(){
            return newLocationCreated;
        }

        protected abstract LocationType getLocationType();
    }
}
