package se.sead.datesradio;

import se.sead.bugsimport.datesradio.seadmodel.DatingUncertainty;
import se.sead.bugsimport.datesradio.seadmodel.Geochronology;
import se.sead.bugsimport.lab.seadmodel.DatingLab;
import se.sead.bugsimport.sample.seadmodel.Sample;
import se.sead.bugsimport.site.seadmodel.SeadSite;
import se.sead.model.TestAnalysisEntity;
import se.sead.model.TestDataset;
import se.sead.model.TestEqualityHelper;
import se.sead.model.TestGeochronology;
import se.sead.repositories.*;
import se.sead.sead.data.AnalysisEntity;
import se.sead.sead.data.DataType;
import se.sead.sead.data.Dataset;
import se.sead.sead.data.DatasetMaster;
import se.sead.sead.methods.Method;
import se.sead.testutils.DatabaseContentVerification;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class DatabaseContentProvider implements DatabaseContentVerification.DatabaseContentTestDataProvider<Geochronology> {

    private static final MathContext TEST_CONTEXT = new MathContext(20, RoundingMode.HALF_DOWN);
    private static final int SCALE = 5;

    private DatasetMaster defaultDatasetMaster;
    private DataType defaultDataType;
    private Method c14StdMethod;
    private GeochronologyRepository geochronologyRepository;
    private DatingLab defaultDatingLab;
    private DatingLab unknownDatingLab;
    private Sample defaultSample;
    private DatingUncertainty greaterThan;

    public DatabaseContentProvider(
            DatasetMasterRepository datasetMasterRepository,
            DataTypeRepository dataTypeRepository,
            MethodRepository methodRepository,
            GeochronologyRepository geochronologyRepository,
            DatingLabRepository datingLabRepository,
            SampleRepository sampleRepository,
            DatingUncertaintyRepository datingUncertaintyRepository) {
        this.defaultDatasetMaster = datasetMasterRepository.findOne(1);
        this.defaultDataType = dataTypeRepository.findOne(1);
        this.c14StdMethod = methodRepository.findOne(3);
        this.geochronologyRepository = geochronologyRepository;
        this.defaultDatingLab = datingLabRepository.findOne(1);
        this.unknownDatingLab = datingLabRepository.findOne(2);
        this.defaultSample = sampleRepository.findOne(1);
        this.greaterThan = datingUncertaintyRepository.findOne(1);
    }

    @Override
    public List<Geochronology> getExpectedData() {
        return Arrays.asList(
                TestGeochronology.create(
                        1,
                        createAe(1,1,"DATE000001"),
                        defaultDatingLab,
                        null,
                        "lab-00001",
                        createNumericVersion(6000),
                        createNumericVersion(70),
                        createNumericVersion(70),
                        "previously inserted not updated bugs dating"
                ),
                TestGeochronology.create(
                        2,
                        createAe(2,2,"DATE000003"),
                        defaultDatingLab,
                        null,
                        "lab-00003",
                        createNumericVersion(6000),
                        createNumericVersion(80),
                        createNumericVersion(80),
                        "updated"
                ),
                TestGeochronology.create(
                        3,
                        createAe(3,3,"DATE000004"),
                        defaultDatingLab,
                        greaterThan,
                        "lab-00004",
                        createNumericVersion(6000),
                        createNumericVersion(70),
                        createNumericVersion(70),
                        "previously inserted not updated with uncertainty"
                ),
                TestGeochronology.create(
                        4,
                        createAe(4,4,"DATE000012"),
                        defaultDatingLab,
                        null,
                        "lab-00012",
                        createNumericVersion(6000),
                        createNumericVersion(70),
                        createNumericVersion(70),
                        "previously inserted and later updated"
                ),
                TestGeochronology.create(
                        null,
                        createAe(null,null,"DATE000002"),
                        defaultDatingLab,
                        null,
                        "lab-00002",
                        createNumericVersion(6000),
                        createNumericVersion(70),
                        createNumericVersion(70),
                        "insert new dating item"
                ),
//                TestGeochronology.create(
//                        null,
//                        createAe(null,null,"DATE000005"),
//                        defaultDatingLab,
//                        unknown,
//                        "lab-00005",
//                        new BigDecimal(6000),
//                        new BigDecimal(70),
//                        new BigDecimal(70),
//                        "insert with uncertainty, default method"
//                ),
                TestGeochronology.create(
                        null,
                        createAe(null,null,"DATE000006"),
                        defaultDatingLab,
                        null,
                        "lab-00006",
                        createNumericVersion(6000),
                        createNumericVersion(70),
                        createNumericVersion(50),
                        "insert with upper and lower errors"
                ),
                TestGeochronology.create(
                        null,
                        createAe(null, null, "DATE000013"),
                        unknownDatingLab,
                        greaterThan,
                        "abc-1",
                        createNumericVersion(6000),
                        createNumericVersion(100),
                        createNumericVersion(100),
                        "Lab not set"
                ),
                TestGeochronology.create(
                        null,
                        createAe(null, null, "DATE000014"),
                        unknownDatingLab,
                        greaterThan,
                        "abc-2",
                        createNumericVersion(6000),
                        createNumericVersion(100),
                        createNumericVersion(100),
                        "Lab not set"
                )
        );
    }

    private AnalysisEntity createAe(Integer aeId, Integer datasetId, String datasetName){
        return TestAnalysisEntity.create(
                aeId,
                TestDataset.create(
                        datasetId,
                        datasetName,
                        c14StdMethod,
                        defaultDatasetMaster,
                        defaultDataType
                ),
                defaultSample
        );
    }

    private BigDecimal createNumericVersion(int value) {
        BigDecimal bdValue = new BigDecimal(value, TEST_CONTEXT);
        bdValue.setScale(SCALE);
        return bdValue;
    }

    @Override
    public List<Geochronology> getActualData() {
        return geochronologyRepository.findAll();
    }

    @Override
    public Comparator<Geochronology> getSorter() {
        return null;
    }

    @Override
    public TestEqualityHelper<Geochronology> getEqualityHelper() {
        TestEqualityHelper<Geochronology> equalityHelper = new TestEqualityHelper<>(true);
        equalityHelper.addMethodInformation(new TestEqualityHelper.ClassMethodInformation(SeadSite.class, "getSiteLocations"));
        equalityHelper.addMethodInformation(new TestEqualityHelper.ClassMethodInformation(Dataset.class, "getContacts"));
        return equalityHelper;
    }

    @Override
    public boolean useEqualityHelper(Geochronology expected, Geochronology actual) {
        return true;
    }

    private static class GeochronologyComparator implements Comparator<Geochronology> {
        @Override
        public int compare(Geochronology o1, Geochronology o2) {
            return o1.getLabSampleNumber().compareTo(o2.getLabSampleNumber());
        }
    }
}
