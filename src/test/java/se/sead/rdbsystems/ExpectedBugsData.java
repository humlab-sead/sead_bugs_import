package se.sead.rdbsystems;

import se.sead.bugsimport.rdbsystems.bugsmodel.BugsRDBSystem;

import java.util.Arrays;
import java.util.List;

public class ExpectedBugsData {

    static final List<BugsRDBSystem> EXPECTED_DATA =
            Arrays.asList(
                create(1, "UKRDB", null, null, null, "Hyman 1992", "UK"),
                create(2, "IUCN", "3.1", 2001, 1994, "IUCN 2001", "Int"),
                create(3, "IUCN", null, 2000, 1994, "Gärdenfors 2000", "Swe"),
                create(4, "TestInsert", "1", null, null, "Test 2000", "Swe"),
                create(5, "TestUpdate", "3.1.1", 1999, 1894, "Updated 1894", "Int"),
                create(6, "TestNoCou", "1", 2000, 2000, "Test 2000", null),
                create(7, null, null, null, null, "Error no System", "Swe"),
                create(8, "TestFailCo", "1", null, null, "Error country does not exists", "BB"),
                create (9, "OldUpdate", "1", 2000, 2000, "Gärdenfors 2000", "Swe")
            );

    private static BugsRDBSystem create(
            Integer systemCode,
            String system,
            String version,
            Integer systemDate,
            Integer firstPublished,
            String ref,
            String countryCode ){
        BugsRDBSystem rdbSystem = new BugsRDBSystem();
        rdbSystem.setRdbSystemCode(systemCode);
        rdbSystem.setRdbSystem(system);
        rdbSystem.setRdbVersion(version);
        rdbSystem.setRdbSystemDate(systemDate);
        rdbSystem.setRdbFirstPublished(firstPublished != null ? firstPublished.shortValue() : null);
        rdbSystem.setRef(ref);
        rdbSystem.setCountryCode(countryCode);
        return rdbSystem;
    }
}
