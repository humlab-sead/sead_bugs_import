package se.sead.bugsimport.site.locations;

import se.sead.bugsimport.country.seadmodel.Location;
import se.sead.bugsimport.country.seadmodel.LocationType;
import se.sead.bugsimport.site.bugsmodel.BugsSite;
import se.sead.repositories.LocationRepository;
import se.sead.repositories.LocationTypeRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

class RegionLocationHandler extends LocationHandler.BaseLocationHandler{

    private LocationType administrativeRegionType;
    private LocationRepository locationRepository;

    private List<RegionListReductionRule> reductionRules;

    public RegionLocationHandler(LocationTypeRepository locationTypeRepository, LocationRepository locationRepository, BugsSite bugsData) {
        super(bugsData);
        this.administrativeRegionType = locationTypeRepository.getAdministrativeRegionType();
        this.locationRepository = locationRepository;
        reductionRules = Arrays.asList(
                new ExcludeTypeReductionRule(locationTypeRepository.getCountryType()),
                new IncludeOnlyTypeReductionRule(administrativeRegionType)
        );
    }

    @Override
    List<Location> getLocations(){
        Location translatedVersion = getTranslatedRegionLocation();
        if(translatedVersion != null){
            return Arrays.asList(translatedVersion);
        }
        String region = bugsData.getRegion();
        if(region != null){
            Location regionLocation = getRegionalVersion(region);
            if(regionLocation == null){
                regionLocation = createLocation(region);
            }
            return Arrays.asList(regionLocation);
        }
        return Collections.EMPTY_LIST;
    }

    private Location getTranslatedRegionLocation(){
        return null;
    }

    private Location getRegionalVersion(String regionName){
        List<Location> regionValues = reduceSet(locationRepository.findAllByName(regionName));
        if(regionValues.size() == 1){
            return regionValues.get(0);
        }
        return null;
    }

    private List<Location> reduceSet(List<Location> locations){
        for (RegionListReductionRule reductionRule :
                reductionRules) {
            if (locations.size() < 2){
                break;
            }
            locations = reductionRule.removeItems(locations);
        }
        return locations;
    }

    @Override
    protected LocationType getLocationType() {
        return administrativeRegionType;
    }

    private static interface RegionListReductionRule {
        List<Location> removeItems(List<Location> regionValues);
    }

    private static class ExcludeTypeReductionRule implements RegionListReductionRule {

        private LocationType removeableType;

        public ExcludeTypeReductionRule(LocationType removeableType) {
            this.removeableType = removeableType;
        }

        @Override
        public List<Location> removeItems(List<Location> regionValues) {
            return regionValues.stream()
                    .filter(region -> !region.getType().equals(removeableType))
                    .collect(Collectors.toList());
        }
    }

    private static class IncludeOnlyTypeReductionRule implements RegionListReductionRule {

        private LocationType includeType;

        public IncludeOnlyTypeReductionRule(LocationType includeType) {
            this.includeType = includeType;
        }

        @Override
        public List<Location> removeItems(List<Location> regionValues) {
            return regionValues.stream()
                    .filter(region -> region.getType().equals(includeType))
                    .collect(Collectors.toList());
        }
    }
}
