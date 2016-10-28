package se.sead.ecocodedefinition;

import se.sead.bugsimport.ecocodedefinition.seadmodel.EcocodeDefinition;
import se.sead.model.TestEcocodeDefinition;
import se.sead.repositories.EcocodeGroupRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ImportTestDefinition {

    private EcocodeGroupRepository groupRepository;

    ImportTestDefinition(EcocodeGroupRepository groupRepository){
        this.groupRepository = groupRepository;
    }

    public List<EcocodeDefinition> getExpectedData(){
        return Collections.emptyList();
/*        List<EcocodeDefinition> expectedData = new ArrayList<>();
        expectedData.add(create(1, "Ecoab", "in trees.", "arboricolous", "imported previously"));
        expectedData.add(create(2, "Ecoag", "agarics", "agaricolous", "added without import"));
        expectedData.add(create(3, "Ecoak", "in tree tops", "akrodendric", "update this"));
        expectedData.add(create(4, "Ecoap", "in reeds.", "arundicolous", "sead data updated after initial import"));
        expectedData.add(create(null, "Ecoar", "on sand.", "arenicolous", "insert"));*/
        //return expectedData;
    }

//    private EcocodeDefinition create(Integer id, String abbrev, String def, String label, String notes){
//        return TestEcocodeDefinition.create(id, def, abbrev, label, notes, )
//    }
}
