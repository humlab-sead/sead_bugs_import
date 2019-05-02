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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

class DatasetDatabaseContentProvider implements DatabaseContentVerification.DatabaseContentTestDataProvider<Dataset>{

    private DatasetMaster bugsDataset;
    private DatasetRepository datasetRepository;
    private DataType defaultDataType;
    private Method palaeoentomology;
    private boolean allowDatasetUpdates;

    DatasetDatabaseContentProvider(
            DataTypeRepository dataTypeRepository,
            MethodRepository methodRepository,
            DatasetMasterRepository datasetMasterRepository,
            DatasetRepository datasetRepository,
            boolean allowDatasetUpdates
    ){
        bugsDataset = datasetMasterRepository.findBugsMasterSet();
        defaultDataType = dataTypeRepository.findOne(1);
        palaeoentomology = methodRepository.findOne(3);
        this.datasetRepository = datasetRepository;
        this.allowDatasetUpdates = allowDatasetUpdates;
    }


    @Override
    public List<Dataset> getExpectedData() {
        Dataset baseCOUN00004Dataset = TestDataset.create(
                3,
                "COUN000004",
                palaeoentomology,
                bugsDataset,
                defaultDataType
        );
        List<Dataset> expectedDatasets = Arrays.asList(
                TestDataset.create(
                        1,
                        "COUN000001",
                        palaeoentomology,
                        bugsDataset,
                        defaultDataType
                ),
                TestDataset.create(
                        2,
                        "COUN000002",
                        palaeoentomology,
                        bugsDataset,
                        defaultDataType
                ),
                TestDataset.create(
                        null,
                        "COUN000003",
                        palaeoentomology,
                        bugsDataset,
                        defaultDataType
                ),
                baseCOUN00004Dataset
        );
        if(!allowDatasetUpdates){
           expectedDatasets = new ArrayList<>(expectedDatasets);
           expectedDatasets.add(TestDataset.create(
                   null,
                   "COUN000004",
                   palaeoentomology,
                   bugsDataset,
                   defaultDataType,
                   baseCOUN00004Dataset
           ));
        }
        return expectedDatasets;
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
            if(o1.getName().equals(o2.getName())){
                return checkForUpdatedDataset(o1,o2);
            }
            return o1.getName().compareTo(o2.getName());
        }

        private int checkForUpdatedDataset(Dataset o1, Dataset o2){
            if(o1.getUpdatedDataset() != null){
                return 1;
            }
            return -1;
        }
    }
}
