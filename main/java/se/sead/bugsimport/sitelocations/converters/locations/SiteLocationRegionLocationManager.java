package se.sead.bugsimport.sitelocations.converters.locations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.locations.LocationCreator;
import se.sead.bugsimport.locations.seadmodel.Location;
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
public class SiteLocationRegionLocationManager extends SiteLocationManagerAccessor {
    @Autowired
    private LocationRepository locationRepository;
    private LocationCreator locationCreator;
    private LocationType administrativeRegionType;

    @Autowired
    public SiteLocationRegionLocationManager(
            SiteLocationRepository siteLocationRepository,
            SiteLocationTraceHelper traceHelper,
            SiteFromCodeDisallowDeletedSite siteTraceHelper,
            LocationTypeRepository locationTypeRepository){
        super(
                siteLocationRepository,
                traceHelper,
                siteTraceHelper,
                new RegionNameAndTypeFilter(locationTypeRepository.getAdministrativeRegionType())
        );
        this.administrativeRegionType = locationTypeRepository.getAdministrativeRegionType();
        this.locationCreator = new LocationCreator(administrativeRegionType);
    }

    @Override
    protected Location searchDatabase(BugsSiteLocation bugsSiteLocation) {
        List<Location> potentialLocations = locationRepository.findAllByName(bugsSiteLocation.getRegion());
        if(!potentialLocations.isEmpty()){
            Optional<Location> administrativeRegionLocation = findAdministrativeLocation(potentialLocations);
            if(administrativeRegionLocation.isPresent()){
                return administrativeRegionLocation.get();
            }
        }
        return null;
    }

    private Optional<Location> findAdministrativeLocation(List<Location> potentialLocations){
        return  potentialLocations.stream()
                .filter(location -> location.getType().equals(administrativeRegionType))
                .findFirst();
    }

    @Override
    protected Location create(BugsSiteLocation bugsSiteLocation) {
        return locationCreator.create(bugsSiteLocation.getRegion());
    }

    private static class RegionNameAndTypeFilter implements NameAndTypeFilter {
        private LocationType regionType;

        RegionNameAndTypeFilter(LocationType locationType){
            this.regionType = locationType;
        }
        @Override
        public SiteLocation filterForNameAndType(BugsSiteLocation bugsSiteLocation, List<SiteLocation> foundDbLocations) {
            Optional<SiteLocation> first = foundDbLocations.stream().filter(siteLocation ->
                    matches(siteLocation.getLocation(), bugsSiteLocation.getRegion())
            ).findFirst();
            if(first.isPresent()){
                return first.get();
            }
            return null;
        }

        private boolean matches(Location location, String name){
            return regionType.equals(location.getType()) &&
                    location.getName().equals(name);
        }
    }
}
