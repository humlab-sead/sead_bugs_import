package se.sead.ecocodedefinition.bugs;

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

    private EcocodeGroup bugsGroup;
    private EcocodeDefinitionRepository definitionRepository;

    DatabaseContentProvider(EcocodeGroupRepository groupRepository, EcocodeDefinitionRepository definitionRepository){
        bugsGroup = groupRepository.findOne(1);
        this.definitionRepository = definitionRepository;
    }

    @Override
    public List<EcocodeDefinition> getExpectedData() {
        return Arrays.asList(
                TestEcocodeDefinition.create(
                        1,
                        "Item exists",
                        "Exists",
                        "Exists",
                        "with notes",
                        bugsGroup
                ),
                TestEcocodeDefinition.create(
                        2,
                        "Update item",
                        "Upd",
                        "Updated",
                        "with notes",
                        bugsGroup
                ),
                TestEcocodeDefinition.create(
                        3,
                        "This value should not be updated",
                        "UpdE",
                        "Fail update",
                        "with notes",
                        bugsGroup
                ),
                TestEcocodeDefinition.create(
                        null,
                        "New item",
                        "New",
                        "New",
                        "with notes",
                        bugsGroup
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
