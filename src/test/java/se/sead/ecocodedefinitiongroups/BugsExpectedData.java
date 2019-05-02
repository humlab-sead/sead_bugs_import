package se.sead.ecocodedefinitiongroups;

import se.sead.bugsimport.ecocodedefinitiongroups.bugsmodel.EcoDefGroups;

import java.util.Arrays;
import java.util.List;

public class BugsExpectedData {

    public static final List<EcoDefGroups> EXPECTED_ACCESS_DATA =
            Arrays.asList(
                create("Ext", "Existing group"),
                    create("New", "New group"),
                    create("Upd", "Updated group"),
                    create("Fai", null)
            );

    private static EcoDefGroups create(String code, String name){
        EcoDefGroups group = new EcoDefGroups();
        group.setEcoName(name);
        group.setEcoGroupCode(code);
        return group;
    }
}
