package se.sead.bugsimport.sitelocations.converters.locations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.locations.seadmodel.Location;
import se.sead.bugsimport.locations.CountryCreator;
import se.sead.bugsimport.locations.LocationCreator;
import se.sead.bugsimport.locations.seadmodel.LocationType;
import se.sead.bugsimport.site.helper.SiteFromCodeDisallowDeletedSite;
import se.sead.bugsimport.sitelocations.bugsmodel.BugsSiteLocation;
import se.sead.bugsimport.sitelocations.converters.SiteLocationTraceHelper;
import se.sead.bugsimport.sitelocations.seadmodel.SiteLocation;
import se.sead.repositories.LocationRepository;
import se.sead.repositories.LocationTypeRepository;
import se.sead.repositories.SiteLocationRepository;

import java.util.List;
import java.util.Optional;

@Component
public class SiteLocationCountrySiteLocationManager extends SiteLocationManagerAccessor {

    @Value("${allow.create.country:true}")
    private boolean canCreateCountries;
    @Autowired
    private LocationRepository locationRepository;
    private LocationTypeRepository locationTypeRepository;
    private LocationCreator locationCreator;

    @Autowired
    public SiteLocationCountrySiteLocationManager(
            LocationTypeRepository locationTypeRepository,
            SiteLocationRepository siteLocationRepository,
            SiteFromCodeDisallowDeletedSite siteTraceHelper,
            SiteLocationTraceHelper siteLocationTraceHelper){
        super(siteLocationRepository, siteLocationTraceHelper, siteTraceHelper, new CountryNameAndTypeFilter(locationTypeRepository.getCountryType()));
        this.locationTypeRepository = locationTypeRepository;
    }

    @Override
    protected Location searchDatabase(BugsSiteLocation bugsSiteLocation) {
        return locationRepository.findCountryByName(bugsSiteLocation.getCountry());
    }

    @Override
    public Location create(BugsSiteLocation bugsSiteLocation){
        if(locationCreator == null){
            locationCreator = new CountryCreator(locationTypeRepository, canCreateCountries);
        }
        return locationCreator.create(bugsSiteLocation.getCountry());
    }

    private static class CountryNameAndTypeFilter implements NameAndTypeFilter {
        private LocationType countryType;
        CountryNameAndTypeFilter(LocationType countryType){
            this.countryType = countryType;
        }

        @Override
        public SiteLocation filterForNameAndType(BugsSiteLocation bugsSiteLocation, List<SiteLocation> foundDbLocations) {
            Optional<SiteLocation> firstFound = foundDbLocations.stream().filter(siteLocation -> {
                Location location = siteLocation.getLocation();
                return location.getType().equals(countryType) && location.getName().equals(bugsSiteLocation.getCountry());
            }).findFirst();
            if(firstFound.isPresent()){
                return firstFound.get();
            } else {
                return null;
            }
        }
    }
}
