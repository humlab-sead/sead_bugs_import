package se.sead.datasetcontacts;

import se.sead.model.TestContact;
import se.sead.model.TestDataset;
import se.sead.model.TestDatasetContact;
import se.sead.model.TestEqualityHelper;
import se.sead.repositories.*;
import se.sead.sead.data.Dataset;
import se.sead.sead.data.DatasetContact;
import se.sead.sead.data.DatasetMaster;
import se.sead.testutils.DatabaseContentVerification;

import java.util.Arrays;
import java.util.Comparator;
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
                ),
                TestDatasetContact.create(
                        null,
                        TestContact.create(3, null, null, null, null, null, "First", null),
                        contactTypeRepository.getIdentifiedByType(),
                        TestDataset.create(6,"COUN000006",methodRepository.findOne(3),bugsMaster, dataTypeRepository.findOne(1))
                ),
                TestDatasetContact.create(
                        null,
                        TestContact.create(null, null, null, null, null, null, "Arnold (only half)", null),
                        contactTypeRepository.getIdentifiedByType(),
                        TestDataset.create(6,"COUN000006",methodRepository.findOne(3),bugsMaster, dataTypeRepository.findOne(1))
                )
        );
    }

    @Override
    public List<DatasetContact> getActualData() {
        return datasetContactRepository.findAll();
    }

    @Override
    public Comparator<DatasetContact> getSorter() {
        return new DatasetContactComparator();
    }

    @Override
    public TestEqualityHelper<DatasetContact> getEqualityHelper() {
        return new TestEqualityHelper<>();
    }

    private static class DatasetContactComparator implements Comparator<DatasetContact> {
        private ContactDatabaseContentProvider.ContactComparator contactComparator = new ContactDatabaseContentProvider.ContactComparator();
        @Override
        public int compare(DatasetContact o1, DatasetContact o2) {
            if(o1.getDataset().equals(o2.getDataset())) {
                if (o1.getType().equals(o2.getType())) {
                    return contactComparator.compare(o1.getContact(), o2.getContact());
                } else {
                    return o1.getType().getName().compareTo(o2.getType().getName());
                }
            } else {
                return compareDatasets(o1.getDataset(), o2.getDataset());
            }
        }

        private int compareDatasets(Dataset o1, Dataset o2){
            return o1.getId().compareTo(o2.getId());
        }
    }
}
