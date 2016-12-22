package se.sead.datasetcontacts;

import se.sead.model.TestContact;
import se.sead.model.TestDataset;
import se.sead.model.TestDatasetContact;
import se.sead.repositories.*;
import se.sead.sead.data.DatasetContact;
import se.sead.sead.data.DatasetMaster;
import se.sead.testutils.DatabaseContentVerification;

import java.util.Arrays;
import java.util.List;

class DatasetContactDatabaseContentProvider implements DatabaseContentVerification.DatabaseContentTestDataProvider<DatasetContact>{

    private ContactTypeRepository contactTypeRepository;
    private MethodRepository methodRepository;
    private DatasetMaster bugsMaster;
    private DataTypeRepository dataTypeRepository;
    private DatasetContactRepository datasetContactRepository;

    DatasetContactDatabaseContentProvider(
            ContactTypeRepository contactTypeRepository,
            MethodRepository methodRepository,
            DatasetMasterRepository masterRepository,
            DataTypeRepository dataTypeRepository,
            DatasetContactRepository datasetContactRepository){
        this.contactTypeRepository = contactTypeRepository;
        this.methodRepository = methodRepository;
        this.bugsMaster = masterRepository.findBugsMasterSet();
        this.dataTypeRepository = dataTypeRepository;
        this.datasetContactRepository = datasetContactRepository;
    }

    @Override
    public List<DatasetContact> getExpectedData() {
        return Arrays.asList(
                TestDatasetContact.create(
                        1,
                        TestContact.create(1, null, null, null, null, "University of Ottawa", null, null),
                        contactTypeRepository.getSpecimentRepositoryType(),
                        TestDataset.create(3,"COUN000003",methodRepository.findOne(3),bugsMaster, dataTypeRepository.findOne(1))
                ),
                TestDatasetContact.create(
                        2,
                        TestContact.create(2, null, null, null, null, "University of Bolivia", null, null),
                        contactTypeRepository.getSpecimentRepositoryType(),
                        TestDataset.create(4,"COUN000004",methodRepository.findOne(3),bugsMaster, dataTypeRepository.findOne(1))
                ),
                TestDatasetContact.create(
                        3,
                        TestContact.create(3, null, null, null, null, null, "First", null),
                        contactTypeRepository.getIdentifiedByType(),
                        TestDataset.create(4,"COUN000004",methodRepository.findOne(3),bugsMaster, dataTypeRepository.findOne(1))
                ),
                TestDatasetContact.create(
                        4,
                        TestContact.create(4, null, null, null, null, null, "Second", null),
                        contactTypeRepository.getIdentifiedByType(),
                        TestDataset.create(4,"COUN000004",methodRepository.findOne(3),bugsMaster, dataTypeRepository.findOne(1))
                ),
                TestDatasetContact.create(
                        null,
                        TestContact.create(null, null, null, null, null, null, "Archer", null),
                        contactTypeRepository.getIdentifiedByType(),
                        TestDataset.create(2,"COUN000002",methodRepository.findOne(3),bugsMaster, dataTypeRepository.findOne(1))
                ),
                TestDatasetContact.create(
                        null,
                        TestContact.create(null, null, null, null, null, "University of Carpatia", null, null),
                        contactTypeRepository.getSpecimentRepositoryType(),
                        TestDataset.create(2,"COUN000002",methodRepository.findOne(3),bugsMaster, dataTypeRepository.findOne(1))
                ),
                TestDatasetContact.create(
                        null,
                        TestContact.create(null, null, null, null, null, null, "A new person", null),
                        contactTypeRepository.getIdentifiedByType(),
                        TestDataset.create(3,"COUN000003",methodRepository.findOne(3),bugsMaster, dataTypeRepository.findOne(1))
                ),
                TestDatasetContact.create(
                        null,
                        TestContact.create(null, null, null, null, null, "University of Carpatia", null, null),
                        contactTypeRepository.getSpecimentRepositoryType(),
                        TestDataset.create(5,"COUN000005",methodRepository.findOne(3),bugsMaster, dataTypeRepository.findOne(1))
                )
        );
    }

    @Override
    public List<DatasetContact> getActualData() {
        return datasetContactRepository.findAll();
    }
}
