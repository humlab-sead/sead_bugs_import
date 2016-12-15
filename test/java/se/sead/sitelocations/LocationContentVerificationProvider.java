package se.sead.sitelocations;

import se.sead.bugsimport.locations.seadmodel.Location;
import se.sead.bugsimport.locations.seadmodel.LocationType;
import se.sead.bugsimport.sitelocations.seadmodel.SiteLocation;
import se.sead.model.TestEqualityHelper;
import se.sead.model.TestLocation;
import se.sead.repositories.LocationRepository;
import se.sead.repositories.LocationTypeRepository;
import se.sead.testutils.DatabaseContentVerification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class LocationContentVerificationProvider implements DatabaseContentVerification.DatabaseContentTestDataProvider<Location> {

    private ImportTestDefinition importTestDefinition;
    private boolean canCreateCountry;
    private LocationRepository locationRepository;
    private LocationTypeRepository locationTypeRepository;

    public LocationContentVerificationProvider(boolean canCreateCountry, ImportTestDefinition importTestDefinition, LocationRepository locationRepository, LocationTypeRepository locationTypeRepository) {
        this.importTestDefinition = importTestDefinition;
        this.canCreateCountry = canCreateCountry;
        this.locationRepository = locationRepository;
        this.locationTypeRepository = locationTypeRepository;
    }

    @Override
    public List<Location> getExpectedData() {
        return Arrays.asList(
                locationRepository.findOne(1),
                locationRepository.findOne(2),
                locationRepository.findOne(3),
                locationRepository.findOne(4),
                locationRepository.findOne(5),
                locationRepository.findOne(6),
                locationRepository.findOne(7),
                locationRepository.findOne(8),
                TestLocation.create(null, "Nonexisting", locationTypeRepository.getCountryType()),
                TestLocation.create(null, "Nonexisting", locationTypeRepository.getBugsUnprocessedBugsTransfer())
        );
    }

    @Override
    public List<Location> getActualData() {
        List<Location> allLocations = new ArrayList<>();
        allLocations.addAll(locationRepository.findByTypeOrderByName(locationTypeRepository.getCountryType()));
        allLocations.addAll(locationRepository.findByTypeOrderByName(locationTypeRepository.getAdministrativeRegionType()));
        allLocations.addAll(locationRepository.findByTypeOrderByName(locationTypeRepository.getAggregateNonAdministrativeRegionType()));
        allLocations.addAll(locationRepository.findByTypeOrderByName(locationTypeRepository.getBugsUnprocessedBugsTransfer()));
        return allLocations;
    }

    @Override
    public Comparator<Location> getSorter() {
        return new LocationComparator();
    }

    @Override
    public TestEqualityHelper<Location> getEqualityHelper() {
        return new TestEqualityHelper<>();
    }

    private static class LocationComparator implements Comparator<Location> {
        @Override
        public int compare(Location o1, Location o2) {
            int typeDifference = compare(o1.getType(), o2.getType());
            if(typeDifference == 0){
                return o1.getName().compareTo(o2.getName());
            }
            return typeDifference;
        }

        private int compare(LocationType o1, LocationType o2){
            if(o1 == null && o2 == null){
                return 0;
            } else if(o1 != null && o2 == null){
                return -1;
            } else if(o1 == null && o2 != null){
                return 1;
            } else {
                return o1.getId().compareTo(o2.getId());
            }
        }
    }
}
