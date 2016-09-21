package se.sead.rdbcodes;

import se.sead.bugsimport.rdbcodes.bugsmodel.BugsRDBCodes;

import java.util.Arrays;
import java.util.List;

public class ExpectedBugsData {

    static List<BugsRDBCodes> EXPECTED_DATA =
            Arrays.asList(
                create(1, "A", "normal insert", 1),
                    create(2, "AB", "already stored w. trace", 1),
                    create(3, "AC", "already added w/o import", 1),
                    create(4, "AD", "update", 1),
                    create(5, null, "error no category", 1),
                    create(6, "E1", "error no system", 0)
            );

    private static BugsRDBCodes create(Integer code, String category, String definition, Integer systemCode){
        BugsRDBCodes rdbCodes = new BugsRDBCodes();
        rdbCodes.setRdbCode(code);
        rdbCodes.setCategory(category);
        rdbCodes.setRdbDefinition(definition);
        rdbCodes.setRdbSystemCode(systemCode);
        return rdbCodes;
    }
}
