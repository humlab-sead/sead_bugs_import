package se.sead.datesperiod;

import se.sead.bugsimport.datesperiod.seadmodel.RelativeDate;
import se.sead.bugsimport.datesradio.seadmodel.DatingUncertainty;
import se.sead.bugsimport.periods.seadmodel.RelativeAge;
import se.sead.bugsimport.sample.seadmodel.Sample;
import se.sead.model.TestAnalysisEntity;
import se.sead.model.TestDataset;
import se.sead.model.TestEqualityHelper;
import se.sead.model.TestRelativeDate;
import se.sead.repositories.*;
import se.sead.sead.data.DataType;
import se.sead.sead.data.Dataset;
import se.sead.sead.data.DatasetMaster;
import se.sead.sead.methods.Method;
import se.sead.testutils.DatabaseContentVerification;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

class DatabaseContentProvider implements DatabaseContentVerification.DatabaseContentTestDataProvider<RelativeDate> {

    private RelativeDateRepository relativeDateRepository;
    private Sample defaultSample;
    private DatingUncertainty greaterThanUncertainty;
    private Method geolPerC14Method;
    private Method geolPerRadioMethod;
    private Method archPerCalMethod;
    private Method unknownCalDating;
    private RelativeAge existingAge;
    private RelativeAge calPer;
    private RelativeAge radio;
    private DatasetMaster bugsMaster;
    private DataType countedDates;
    private DataType uncalibratedDates;

    public DatabaseContentProvider(
            SampleRepository sampleRepository,
            DatingUncertaintyRepository datingUncertaintyRepository,
            MethodRepository methodRepository,
            RelativeAgeRepository relativeAgeRepository,
            RelativeDateRepository relativeDateRepository,
            DatasetMasterRepository datasetMasterRepository,
            DataTypeRepository dataTypeRepository
    ){
        defaultSample = sampleRepository.findOne(1);
        greaterThanUncertainty = datingUncertaintyRepository.findOne(1);
        geolPerC14Method = methodRepository.findOne(4);
        archPerCalMethod = methodRepository.findOne(3);
        geolPerRadioMethod = methodRepository.findOne(5);
        unknownCalDating = methodRepository.findOne(6);
        existingAge = relativeAgeRepository.findOne(1);
        calPer = relativeAgeRepository.findOne(2);
        radio = relativeAgeRepository.findOne(3);
        bugsMaster = datasetMasterRepository.findOne(1);
        uncalibratedDates = dataTypeRepository.findOne(1);
        countedDates = dataTypeRepository.findOne(2);
        this.relativeDateRepository = relativeDateRepository;
    }

    @Override
    public List<RelativeDate> getExpectedData() {
        return
                Arrays.asList(
                        TestRelativeDate.create(
                                1,
                                null,
                                existingAge,
                                "Already stored",
                                TestAnalysisEntity.create(1,
                                        TestDataset.create(1, "PERI000011", geolPerC14Method, bugsMaster, uncalibratedDates),
                                        defaultSample)
                        ),
                        TestRelativeDate.create(
                                2,
                                null,
                                existingAge,
                                "Update",
                                TestAnalysisEntity.create(2,
                                        TestDataset.create(2, "PERI000012", geolPerC14Method, bugsMaster, uncalibratedDates),
                                        defaultSample)
                        ),
                        TestRelativeDate.create(
                                3,
                                null,
                                existingAge,
                                "Sead changed data after import",
                                TestAnalysisEntity.create(3,
                                        TestDataset.create(3, "PERI000013", archPerCalMethod, bugsMaster, uncalibratedDates),
                                        defaultSample)
                        ),
                        TestRelativeDate.create(
                                null,
                                greaterThanUncertainty,
                                existingAge,
                                "No method is ok -insert",
                                TestAnalysisEntity.create(null,
                                        TestDataset.create(null, "PERI000007", unknownCalDating, bugsMaster, uncalibratedDates),
                                        defaultSample)
                        ),
                        TestRelativeDate.create(
                                null,
                                null,
                                existingAge,
                                "uncertainty is null, is ok -insert",
                                TestAnalysisEntity.create(null,
                                        TestDataset.create(null, "PERI000009", geolPerC14Method, bugsMaster, uncalibratedDates),
                                        defaultSample)
                        ),
                        TestRelativeDate.create(
                                null,
                                null,
                                calPer,
                                "ArchPer version calendar",
                                TestAnalysisEntity.create(null,
                                        TestDataset.create(null, "PERI000014", archPerCalMethod, bugsMaster, uncalibratedDates),
                                        defaultSample)
                        ),
                        TestRelativeDate.create(
                                null,
                                null,
                                radio,
                                "GeolPer version radiometric",
                                TestAnalysisEntity.create(null,
                                        TestDataset.create(null,"PERI000015", geolPerRadioMethod, bugsMaster, uncalibratedDates),
                                        defaultSample)
                        )
                );
    }

    @Override
    public List<RelativeDate> getActualData() {
        return relativeDateRepository.findAll();
    }

    @Override
    public Comparator<RelativeDate> getSorter() {
        return new RelativeDateComparator();
    }

    @Override
    public TestEqualityHelper<RelativeDate> getEqualityHelper() {
        TestEqualityHelper<RelativeDate> relativeDateEqualityHelper = new TestEqualityHelper<>();
        relativeDateEqualityHelper.addMethodInformation(
                new TestEqualityHelper.ClassMethodInformation(Dataset.class, "getContacts")
        );
        return relativeDateEqualityHelper;
    }

    private static class RelativeDateComparator implements Comparator<RelativeDate> {
        @Override
        public int compare(RelativeDate o1, RelativeDate o2) {
            return o1.getNotes().compareTo(o2.getNotes());
        }
    }
}
