package se.sead.model;

import se.sead.bugsimport.ecocodedefinitiongroups.seadmodel.EcocodeGroup;
import se.sead.bugsimport.ecocodedefinitiongroups.seadmodel.EcocodeSystem;

public class TestEcocodeGroup extends EcocodeGroup {

    private TestEcocodeGroup(Integer id){
        setId(id);
    }

    public static EcocodeGroup create(
            Integer id,
            String abbreviation,
            String name,
            EcocodeSystem system
    ){
        EcocodeGroup group = new TestEcocodeGroup(id);
        group.setAbbreviation(abbreviation);
        group.setName(name);
        group.setSystem(system);
        return group;
    }
}
