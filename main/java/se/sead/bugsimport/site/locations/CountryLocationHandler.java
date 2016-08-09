package se.sead.bugsimport.site.locations;

import se.sead.bugsimport.country.seadmodel.Location;
import se.sead.bugsimport.country.seadmodel.LocationType;
import se.sead.bugsimport.site.bugsmodel.BugsSite;
import se.sead.repositories.LocationRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class CountryLocationHandler extends LocationHandler.BaseLocationHandler{

    private LocationType countryType;
    private LocationRepository locationRepository;
    private boolean canCreateNonExistingCountry = false;

    public CountryLocationHandler(
            LocationType countryType,
            LocationRepository locationRepository,
            BugsSite bugsData
    ) {
        this(countryType, locationRepository, bugsData, false);
    }

    public CountryLocationHandler(
            LocationType countryType,
            LocationRepository locationRepository,
            BugsSite bugsData,
            boolean canCreateNonExistingCountry
    ) {
        super(bugsData);
        this.countryType = countryType;
        this.locationRepository = locationRepository;
        this.canCreateNonExistingCountry = canCreateNonExistingCountry;
    }

    @Override
    List<Location> getLocations() {
        String country = bugsData.getCountry();
        if(country != null){
            Location countryByName = locationRepository.findCountryByName(country);
            if(countryByName != null){
                return Arrays.asList(
                    countryByName
                );
            } else if(canCreateNonExistingCountry){
                return Arrays.asList(
                    createLocation(country)
                );
            }
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    protected LocationType getLocationType() {
        return countryType;
    }
}
