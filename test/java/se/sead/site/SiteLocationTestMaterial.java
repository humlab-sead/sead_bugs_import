package se.sead.site;

import se.sead.bugsimport.locations.seadmodel.Location;
import se.sead.bugsimport.locations.seadmodel.LocationType;
import se.sead.model.TestEqualityHelper;
import se.sead.model.TestLocation;
import se.sead.repositories.LocationRepository;
import se.sead.repositories.LocationTypeRepository;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SiteLocationTestMaterial {

    private LocationRepository locationRepository;
    private LocationType countryType;
    private LocationType subCountryAdministrativeRegion;
    private LocationType aggregateNonAdminGeographicalRegion;

    private Comparator<Location> comparator;

    public SiteLocationTestMaterial(LocationRepository locationRepository, LocationTypeRepository locationTypeRepository) {
        this.locationRepository = locationRepository;
        countryType = locationTypeRepository.getCountryType();
        subCountryAdministrativeRegion = locationTypeRepository.getAdministrativeRegionType();
        aggregateNonAdminGeographicalRegion = locationTypeRepository.getAggregateNonAdministrativeRegionType();
        comparator = new LocationComparator();
    }

    void assertNoLocationsCreated(){
        List<Location> expected = getExpected();
        List<Location> actual = getActual();
        Collections.sort(expected, comparator);
        Collections.sort(actual, comparator);
        assertEquals(expected, actual);
    }

    public List<Location> getExpected() {
        return Arrays.asList(
                TestLocation.create(
                    105,
                        "Ireland",
                        countryType
                ),
                TestLocation.create(
                        205,
                        "Sweden",
                        countryType
                ),
                TestLocation.create(
                        240,
                        "England",
                        countryType
                ),
                TestLocation.create(
                        590,
                        "Antrim",
                        subCountryAdministrativeRegion
                ),
                TestLocation.create(
                        747,
                        "Oxfordshire",
                        subCountryAdministrativeRegion
                ),
                TestLocation.create(
                        770,
                        "Santorini",
                        aggregateNonAdminGeographicalRegion
                ),
                TestLocation.create(
                        780,
                        "Skåne",
                        subCountryAdministrativeRegion
                ),
                TestLocation.create(
                        777,
                        "Greece",
                        countryType
                )
        );
    }

    public List<Location> getActual() {
        List<Location> all = new ArrayList<>();
        all.addAll(locationRepository.findByTypeOrderByName(countryType));
        all.addAll(locationRepository.findByTypeOrderByName(subCountryAdministrativeRegion));
        all.addAll(locationRepository.findByTypeOrderByName(aggregateNonAdminGeographicalRegion));
        return all;
    }

    private static class LocationComparator implements Comparator<Location> {
        @Override
        public int compare(Location o1, Location o2) {
            return o1.getId().compareTo(o2.getId());
        }
    }
}
