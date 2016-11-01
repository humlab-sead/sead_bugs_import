package se.sead.ecocodedefinition.bugs;

import se.sead.bugsimport.ecocodedefinition.bugs.bugsmodel.EcoDefBugs;

import java.util.Arrays;
import java.util.List;

class ExpectedBugsData {

    static final List<EcoDefBugs> EXPECTED_DATA =
            Arrays.asList(
                    create("Exists", "Item exists", "with notes", "Exists", 0),
                    create("New", "New item", "with notes", "New", 1),
                    create("Upd", "Update item", "with notes", "Updated", 2),
                    create("UpdE", "Sead updated newer", "with notes", "Fail update", 3),
                    create("LabelE", "No label provided", "with notes", null, 4),
                    create("DefE", null, "with notes", "No definition", 5)
            );

    private static EcoDefBugs create(
            String bugsEcoCode,
            String definition,
            String notes,
            String ecoLabel,
            Integer sortOrder
    ){
        EcoDefBugs eco = new EcoDefBugs();
        eco.setBugsEcoCODE(bugsEcoCode);
        eco.setDefinition(definition);
        eco.setNotes(notes);
        eco.setEcoLabel(ecoLabel);
        eco.setSortOrder(sortOrder.shortValue());
        return eco;
    }
}
