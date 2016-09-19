package se.sead.ecocodedefinition;

import se.sead.bugsimport.ecocodedefinition.bugsmodel.EcoDefKoch;

import java.util.Arrays;
import java.util.List;

public class ExpectedBugsAccessData {

    public static final List<EcoDefKoch> EXPECTED_DATA =
            Arrays.asList(
                create("Ecoab", "ab", "arboricolous", "Eco", "in trees.", "imported previously"),
                    create("Ecoag", "ag", "agaricolous", "Eco", "agarics", "added without import"),
                    create("Ecoak", "ak", "akrodendric", "Eco", "in tree tops.", "update this"),
                    create("Ecoap", "ap", "arundicolous", "Eco", "in reeds", "sead data updated after initial import"),
                    create("Ecoaq", "aq", null, "Eco", "in water", "no full name"),
                    create("Ecoar", "ar", "arenicolous", "Eco", "on sand.", "insert")

            );

    private static EcoDefKoch create(String bugsKochCode, String kochCode, String fullName, String kochGroup, String description, String notes){
        EcoDefKoch def = new EcoDefKoch();
        def.setBugsKochCode(bugsKochCode);
        def.setKochCode(kochCode);
        def.setFullName(fullName);
        def.setKochGroup(kochGroup);
        def.setDescription(description);
        def.setNotes(notes);
        return def;
    }
}

