package se.sead.ecocodedefinition.koch;

import se.sead.bugsimport.ecocodedefinition.seadmodel.EcocodeDefinition;
import se.sead.bugsimport.ecocodedefinitiongroups.seadmodel.EcocodeGroup;
import se.sead.ecocodedefinition.TestEcocodeDefinitionComparator;
import se.sead.model.TestEcocodeDefinition;
import se.sead.model.TestEqualityHelper;
import se.sead.repositories.EcocodeDefinitionRepository;
import se.sead.repositories.EcocodeGroupRepository;
import se.sead.testutils.DatabaseContentVerification;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

class DatabaseContentProvider implements DatabaseContentVerification.DatabaseContentTestDataProvider<EcocodeDefinition> {

    private EcocodeGroup grpGroup;
    private EcocodeDefinitionRepository definitionRepository;

    DatabaseContentProvider(EcocodeGroupRepository groupRepository, EcocodeDefinitionRepository definitionRepository){
        grpGroup = groupRepository.findOne(1);
        this.definitionRepository = definitionRepository;
    }

    @Override
    public List<EcocodeDefinition> getExpectedData() {
        return Arrays.asList(
                TestEcocodeDefinition.create(
                        1,
                        "An existing definition",
                        "Exist",
                        "definition exists",
                        "with notes",
                        grpGroup
                ),
                TestEcocodeDefinition.create(
                        2,
                        "A non-problematic update",
                        "Upd",
                        "Update entry",
                        "with notes",
                        grpGroup
                ),
                TestEcocodeDefinition.create(
                        3,
                        "This value should not be updated",
                        "UpdE",
                        "Try update",
                        "with notes",
                        grpGroup
                ),
                TestEcocodeDefinition.create(
                        null,
                        "A non-problematic definition",
                        "New",
                        "New entry",
                        "with notes",
                        grpGroup
                ),
                TestEcocodeDefinition.create(
                        null,
                        "No name specified",
                        "NoN",
                        null,
                        "is ok",
                        grpGroup
                )
        );
    }

    @Override
    public List<EcocodeDefinition> getActualData() {
        return definitionRepository.findAll();
    }

    @Override
    public Comparator<EcocodeDefinition> getSorter() {
        return new TestEcocodeDefinitionComparator();
    }

    @Override
    public TestEqualityHelper<EcocodeDefinition> getEqualityHelper() {
        return new TestEqualityHelper<>();
    }

}
