package se.sead.ecocodes.bugs;

import se.sead.bugsimport.ecocodes.bugs.bugsmodel.EcoBugs;

import java.util.Arrays;
import java.util.List;

class ExpectedBugsData {

    static final List<EcoBugs> EXPECTED_DATA =
            Arrays.asList(
                    create(1d, null), // illegal, no bugsCode specified
                    create(1d, "Error"), // no code found
                    create(1d, "Def"), // already exists
                    create(2d, "Def 2"), // insert
                    create(3d, "Def"), // already stored
                    create(3d, "Def 2"), // insert on code = 3
                    create(99d, "Def") // no species found
            );

    private static EcoBugs create(
            Double code,
            String bugsCode
    ){
        EcoBugs ecoBugs = new EcoBugs();
        ecoBugs.setCode(code);
        ecoBugs.setBugsEcoCode(bugsCode);
        return ecoBugs;
    }
}
