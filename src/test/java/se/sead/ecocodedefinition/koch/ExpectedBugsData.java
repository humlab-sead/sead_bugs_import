package se.sead.ecocodedefinition.koch;

import se.sead.bugsimport.ecocodedefinition.koch.bugsmodel.EcoDefKoch;

import java.util.Arrays;
import java.util.List;

class ExpectedBugsData {

    static final List<EcoDefKoch> EXPECTED_DATA =
            Arrays.asList(
                create("Exist", "ex", "definition exists", "Grp", "An existing definition", "with notes"),
                create("New", "ne", "New entry", "Grp", "A non-problematic definition", "with notes"),
                    create("NoG", "ng", "No group", null, "no group specified", "with notes"),
                    create("NoG2", "nn", "No group exists", "Err", "No group found for code", "with notes"),
                    create("NoN", "n", null, "Grp", "No name specified", "is ok"),
                    create("Upd", "up", "Update entry", "Grp", "A non-problematic update", "with notes"),
                    create("UpdE", "ue", "Try update", "Grp", "Update will fail due to newer sead data", "with notes")
            );

    private static EcoDefKoch create(
        String bugsCode,
        String kochCode,
        String fullName,
        String kochGroup,
        String description,
        String notes
    ){
        EcoDefKoch def = new EcoDefKoch();
        def.setBugsKochCode(bugsCode);
        def.setKochCode(kochCode);
        def.setFullName(fullName);
        def.setKochGroup(kochGroup);
        def.setDescription(description);
        def.setNotes(notes);
        return def;
    }
}
