package se.sead.model;

import se.sead.bugsimport.rdbcodes.seadmodel.RdbCode;
import se.sead.bugsimport.rdbsystems.seadmodel.RdbSystem;

public class TestRdbCode extends RdbCode {

    private TestRdbCode(Integer id){
        super.setId(id);
    }

    public static RdbCode create(Integer id, String category, String definition, RdbSystem system){
        RdbCode code = new TestRdbCode(id);
        code.setCategory(category);
        code.setDefinition(definition);
        code.setSystem(system);
        return code;
    }
}
