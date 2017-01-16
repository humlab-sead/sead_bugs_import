package se.sead.fossil;

import se.sead.model.TestDataset;
import se.sead.model.TestEqualityHelper;
import se.sead.repositories.DataTypeRepository;
import se.sead.repositories.DatasetMasterRepository;
import se.sead.repositories.DatasetRepository;
import se.sead.repositories.MethodRepository;
import se.sead.sead.data.DataType;
import se.sead.sead.data.Dataset;
import se.sead.sead.data.DatasetMaster;
import se.sead.sead.methods.Method;
import se.sead.testutils.DatabaseContentVerification;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

class DatasetDatabaseContentProvider implements DatabaseContentVerification.DatabaseContentTestDataProvider<Dataset>{

    private DatasetMaster bugsDataset;
    private DatasetRepository datasetRepository;
    private DataType defaultDataType;
    private DataType undefinedOther;
    private Method palaeoentomology;
    private Method c14;

    DatasetDatabaseContentProvider(
            DataTypeRepository dataTypeRepository,
            MethodRepository methodRepository,
            DatasetMasterRepository datasetMasterRepository,
            DatasetRepository datasetRepository
    ){
        bugsDataset = datasetMasterRepository.findBugsMasterSet();
        defaultDataType = dataTypeRepository.findOne(1);
        undefinedOther = dataTypeRepository.findOne(2);
        palaeoentomology = methodRepository.findOne(3);
        c14 = methodRepository.findOne(4);
        this.datasetRepository = datasetRepository;
    }


    @Override
    public List<Dataset> getExpectedData() {
        return Arrays.asList(
                TestDataset.create(
                        1,
                        "COUN000001",
                        palaeoentomology,
                        bugsDataset,
                        defaultDataType
                ),
                TestDataset.create(
                        2,
                        "DATE000001",
                        c14,
                        bugsDataset,
                        undefinedOther
                ),
                TestDataset.create(
                        null,
                        "COUN000003",
                        palaeoentomology,
                        bugsDataset,
                        defaultDataType
                )
        );
    }

    @Override
    public List<Dataset> getActualData() {
        return datasetRepository.findAll();
    }

    @Override
    public Comparator<Dataset> getSorter() {
        return new DatasetComparator();
    }

    @Override
    public TestEqualityHelper<Dataset> getEqualityHelper() {
        TestEqualityHelper<Dataset> testEqualityHelper = new TestEqualityHelper<>();
        testEqualityHelper.addMethodInformation(new TestEqualityHelper.ClassMethodInformation(Dataset.class, "getContacts"));
        return testEqualityHelper;
    }

    private static class DatasetComparator implements Comparator<Dataset> {
        @Override
        public int compare(Dataset o1, Dataset o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }
}
