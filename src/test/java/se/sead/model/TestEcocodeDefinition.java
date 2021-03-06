package se.sead.model;

import se.sead.bugsimport.ecocodedefinition.seadmodel.EcocodeDefinition;
import se.sead.bugsimport.ecocodedefinitiongroups.seadmodel.EcocodeGroup;

public class TestEcocodeDefinition extends EcocodeDefinition {

    private TestEcocodeDefinition(Integer id){
        super.setId(id);
    }

    public static EcocodeDefinition create(Integer id, String definition, String abbreviation, String name, String notes, EcocodeGroup group){
        EcocodeDefinition def = new TestEcocodeDefinition(id);
        def.setDefinition(definition);
        def.setAbbreviation(abbreviation);
        def.setName(name);
        def.setNotes(notes);
        def.setGroup(group);
        return def;
    }
}
