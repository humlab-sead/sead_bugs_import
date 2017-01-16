package se.sead.fossil;

import se.sead.bugsimport.sample.seadmodel.Sample;
import se.sead.model.TestAnalysisEntity;
import se.sead.model.TestDataset;
import se.sead.model.TestEqualityHelper;
import se.sead.repositories.*;
import se.sead.sead.data.AnalysisEntity;
import se.sead.sead.data.DataType;
import se.sead.sead.data.Dataset;
import se.sead.sead.data.DatasetMaster;
import se.sead.sead.methods.Method;
import se.sead.testutils.DatabaseContentVerification;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

class AnalysisEntityDatabaseContentProvider implements DatabaseContentVerification.DatabaseContentTestDataProvider<AnalysisEntity> {

    private SampleRepository sampleRepository;
    private Method palaeoentomology;
    private Method c14;
    private DatasetMaster bugsDataset;
    private DataType defaultDataType;
    private DataType undefinedOther;
    private AnalysisEntityRepository analysisEntityRepository;

    AnalysisEntityDatabaseContentProvider(
            SampleRepository sampleRepository,
            DatasetMasterRepository datasetMasterRepository,
            DataTypeRepository dataTypeRepository,
            MethodRepository methodRepository,
            AnalysisEntityRepository analysisEntityRepository) {
        this.sampleRepository = sampleRepository;
        palaeoentomology = methodRepository.findOne(3);
        c14 = methodRepository.findOne(4);
        bugsDataset = datasetMasterRepository.findOne(1);
        defaultDataType = dataTypeRepository.findOne(1);
        undefinedOther = dataTypeRepository.findOne(2);
        this.analysisEntityRepository = analysisEntityRepository;
    }

    @Override
    public List<AnalysisEntity> getExpectedData() {
        return Arrays.asList(
                TestAnalysisEntity.create(
                        1,
                        createDataset(1, "COUN000001"),
                        sampleRepository.findOne(1)
                ),
                TestAnalysisEntity.create(
                        2,
                        createDataset(2, "DATE000001", undefinedOther, c14),
                        sampleRepository.findOne(3)
                ),
                TestAnalysisEntity.create(
                        null,
                        createDataset(1, "COUN000001"),
                        sampleRepository.findOne(2)
                ),
                TestAnalysisEntity.create(
                        null,
                        createDataset(1, "COUN000001"),
                        sampleRepository.findOne(3)
                ),
                TestAnalysisEntity.create(
                        null,
                        createDataset(null, "COUN000003"),
                        sampleRepository.findOne(5)
                )
        );
    }

    private Dataset createDataset(Integer datasetId, String datasetName){
        return createDataset(datasetId, datasetName, defaultDataType, palaeoentomology);
    }

    private Dataset createDataset(Integer datasetId, String datasetName, DataType type, Method method){
        return TestDataset.create(
                datasetId,
                datasetName,
                method,
                bugsDataset,
                type
        );
    }

    @Override
    public List<AnalysisEntity> getActualData() {
        return analysisEntityRepository.findAll();
    }

    @Override
    public Comparator<AnalysisEntity> getSorter() {
        return new AnalysisEntitiesComparator();
    }

    @Override
    public TestEqualityHelper<AnalysisEntity> getEqualityHelper() {
        TestEqualityHelper<AnalysisEntity> testEqualityHelper = new TestEqualityHelper<>();
        testEqualityHelper.addMethodInformation(new TestEqualityHelper.ClassMethodInformation(Dataset.class, "getContacts"));
        return testEqualityHelper;
    }

    private static class AnalysisEntitiesComparator implements Comparator<AnalysisEntity> {
        @Override
        public int compare(AnalysisEntity o1, AnalysisEntity o2) {
            Sample o1Sample = o1.getSample();
            Sample o2Sample = o2.getSample();
            int diff = o1Sample.getId().compareTo(o2Sample.getId());
            if(diff != 0){
                return diff;
            }
            Dataset o1Dataset = o1.getDataset();
            Dataset o2Dataset = o2.getDataset();
            return o1Dataset.getName().compareTo(o2Dataset.getName());
        }
    }
}
