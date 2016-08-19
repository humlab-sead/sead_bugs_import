package se.sead.country;

import se.sead.bugsimport.locations.country.bugsmodel.Country;
import se.sead.bugsimport.locations.seadmodel.Location;
import se.sead.bugsimport.locations.seadmodel.LocationType;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.model.TestLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class CountryExpectedTestDefinition {

    static final String MDB_FILE = "countries.mdb";
    static final List<Country> EXPECTED_COUNTRIES =
            Arrays.asList(
                    create("Eg", "Egypt"),
                    create("Ger", "Germany"),
                    create("Int", "International"),
                    create("Swe", "Sweden"),
                    create("UK", "United Kingdom")
            );

    private static Country create(String code, String name){
        Country country = new Country();
        country.setCountryCode(code);
        country.setCountry(name);
        return country;
    }

    List<Location> getExpectedResults(LocationType countryType){
        List<Location> expected = new ArrayList<>();
        expected.add(TestLocation.create(5, "Andorra", countryType));
        expected.add(TestLocation.create(1, "Egypt", countryType));
        expected.add(TestLocation.create(null, "Germany", countryType));
        expected.add(TestLocation.create(null, "International", countryType));
        expected.add(TestLocation.create(null, "Sweden", countryType));
        expected.add(TestLocation.create(null, "United Kingdom", countryType));
        return expected;
    }


    void assertTraces(Country bugsData, List<BugsTrace> traces) {
        if("Egypt".equals(bugsData.getCountry())){

        } else {
            assertTrue(traces.stream()
                    .map(trace -> trace.getSeadTable())
                    .allMatch(tableName -> "tbl_locations".equals(tableName)));
        }
    }
}
