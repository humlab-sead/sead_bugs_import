package se.sead.country;

import se.sead.bugsimport.locations.seadmodel.Location;
import se.sead.bugsimport.locations.seadmodel.LocationType;
import se.sead.model.TestEqualityHelper;
import se.sead.model.TestLocation;
import se.sead.repositories.LocationRepository;
import se.sead.repositories.LocationTypeRepository;
import se.sead.testutils.DatabaseContentVerification;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class CountryDatabaseContentProvider implements DatabaseContentVerification.DatabaseContentTestDataProvider<Location> {

    private LocationType countryType;
    private LocationRepository locationRepository;

    CountryDatabaseContentProvider(LocationTypeRepository locationTypeRepository, LocationRepository locationRepository) {
        countryType = locationTypeRepository.getCountryType();
        this.locationRepository = locationRepository;
    }

    @Override
    public List<Location> getExpectedData() {
        List<Location> expected = new ArrayList<>();
        expected.add(TestLocation.create(1, "Egypt", countryType));
        expected.add(TestLocation.create(5, "Andorra", countryType));
        expected.add(TestLocation.create(2, "Germany", countryType));
        expected.add(TestLocation.create(null, "International", countryType));
        return expected;
    }

    @Override
    public List<Location> getActualData() {
        return locationRepository.findByTypeOrderByName(countryType);
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
            return o1.compareTo(o2);
        }
    }
}
