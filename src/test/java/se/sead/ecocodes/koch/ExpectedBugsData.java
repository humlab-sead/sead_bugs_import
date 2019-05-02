package se.sead.ecocodes.koch;

import se.sead.bugsimport.ecocodes.koch.bugsmodel.EcoKoch;

import java.util.Arrays;
import java.util.List;

class ExpectedBugsData {

    static final List<EcoKoch> EXPECTED_DATA =
            Arrays.asList(
                    create(1d, null), // illegal, no kochCode specified
                    create(1d, "Error"), // no code found
                    create(1d, "Def"), // already exists
                    create(2d, "Def 2"), // insert
                    create(3d, "Def"), // already stored
                    create(3d, "Def 2"), // insert on code = 3
                    create(99d, "Def"), // no species found
                    create(2d, "def 3") // wrong case, match by ignore case
            );

    private static EcoKoch create(
        Double code,
        String kochCode
    ){
        EcoKoch ecoKoch = new EcoKoch();
        ecoKoch.setCode(code);
        ecoKoch.setBugsKochCode(kochCode);
        return ecoKoch;
    }
}
