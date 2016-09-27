package se.sead.mcrnames;

import se.sead.bugsimport.mcrnames.bugsmodel.BugsMCRNames;

import java.util.Arrays;
import java.util.List;

public class ExpectedBugsData {

    public static final List<BugsMCRNames> EXPECTED_DATA =
            Arrays.asList(
                create(1.001002d, 273, "Cicindela sylvatica L.", "Found similarity", 1.001002d, "CicindelasylvaticaL."),
                create(1.0040140d, 288, "Carabus clathratus L.", "=Carabus clatratus L. 1.004014", 1.004014d, "CarabusclathratusL."),
                create(10d, 1, "No species found", "status", 10d, "Nospeciesfound"),
                create(1d, 1, "Match without traces", "do not import", 1d, "Matchwithouttraces"),
                create(2d, 2, "Update this", "import with update", 2d, "Updatethis"),
                create(3d, 3, "Sead data changed", "do not import", 3d, "Seaddatachanged")
            );

    private static BugsMCRNames create(
            Double code,
            Integer mcrNumber,
            String mcrName,
            String compareStatus,
            Double tempCode,
            String mcrNameTrim
    ){
        BugsMCRNames name = new BugsMCRNames();
        name.setTempCode(tempCode);
        name.setMcrNumber(mcrNumber.shortValue());
        name.setMcrNameTrim(mcrNameTrim);
        name.setMcrName(mcrName);
        name.setCompareStatus(compareStatus);
        name.setCode(code);
        return name;
    }
}
