package se.sead.rdb;

import se.sead.bugsimport.rdb.bugsmodel.BugsRDB;

import java.util.Arrays;
import java.util.List;

public class ExpectedBugData {

    static List<BugsRDB> EXPECTED_DATA =
            Arrays.asList(
                create(1.0010001d, "UK", 1),
                create(1.0010001d, "Svw", 1),
                create(1.0010122d, "Swe", 2),
                create(99d, "UK", 2),
                create(1.0010001d, "UK", 3),
                create(1.0010123d, "UK", 1),
                create(1.0010123d, "UK", 2),
                create(null, "Swe", 1),
                create(1.0010122d, null, 1),
                create(1.0010122d, "Swe", null),
                create(1.0010001d, "Swe", 2)
            );

    private static BugsRDB create(Double code, String countryCode, Integer rdbCode){
        BugsRDB rdb = new BugsRDB();
        rdb.setCountryCode(countryCode);
        rdb.setCode(code);
        rdb.setRdbCode(rdbCode);
        return rdb;
    }
}
