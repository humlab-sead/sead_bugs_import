package se.sead.model;

import se.sead.bugsimport.ecocodedefinition.seadmodel.EcocodeDefinition;
import se.sead.bugsimport.ecocodedefinition.seadmodel.EcocodeGroup;

public class TestEcocodeDefinition extends EcocodeDefinition {

    private TestEcocodeDefinition(Integer id){
        super.setId(id);
    }

    public static EcocodeDefinition create(Integer id, String definition, String abbreviation, String label, String notes, EcocodeGroup group){
        EcocodeDefinition def = new TestEcocodeDefinition(id);
        def.setDefinition(definition);
        def.setAbbreviation(abbreviation);
        def.setLabel(label);
        def.setNotes(notes);
        def.setGroup(group);
        return def;
    }
}
