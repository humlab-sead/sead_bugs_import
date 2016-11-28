package se.sead.datesperiod;

import se.sead.bugsimport.datesperiod.seadmodel.RelativeDate;
import se.sead.bugsimport.datesradio.seadmodel.DatingUncertainty;
import se.sead.bugsimport.periods.seadmodel.RelativeAge;
import se.sead.bugsimport.sample.seadmodel.Sample;
import se.sead.model.TestEqualityHelper;
import se.sead.model.TestRelativeDate;
import se.sead.repositories.*;
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
    private RelativeAge existingAge;
    private RelativeAge calPer;
    private RelativeAge radio;

    public DatabaseContentProvider(
            SampleRepository sampleRepository,
            DatingUncertaintyRepository datingUncertaintyRepository,
            MethodRepository methodRepository,
            RelativeAgeRepository relativeAgeRepository,
            RelativeDateRepository relativeDateRepository
    ){
        defaultSample = sampleRepository.findOne(1);
        greaterThanUncertainty = datingUncertaintyRepository.findOne(1);
        geolPerC14Method = methodRepository.findOne(4);
        archPerCalMethod = methodRepository.findOne(3);
        geolPerRadioMethod = methodRepository.findOne(5);
        existingAge = relativeAgeRepository.findOne(1);
        calPer = relativeAgeRepository.findOne(2);
        radio = relativeAgeRepository.findOne(3);
        this.relativeDateRepository = relativeDateRepository;
    }

    @Override
    public List<RelativeDate> getExpectedData() {
        return
                Arrays.asList(
                        TestRelativeDate.create(
                                1,
                                defaultSample,
                                null,
                                existingAge,
                                geolPerC14Method,
                                "Already stored"
                        ),
                        TestRelativeDate.create(
                                2,
                                defaultSample,
                                null,
                                existingAge,
                                geolPerC14Method,
                                "Update"
                        ),
                        TestRelativeDate.create(
                                3,
                                defaultSample,
                                greaterThanUncertainty,
                                existingAge,
                                archPerCalMethod,
                                "Sead changed data after import"
                        ),
                        TestRelativeDate.create(
                                null,
                                defaultSample,
                                greaterThanUncertainty,
                                existingAge,
                                null,
                                "No method is ok -insert"
                        ),
                        TestRelativeDate.create(
                                null,
                                defaultSample,
                                null,
                                existingAge,
                                geolPerC14Method,
                                "uncertainty is null, is ok -insert"
                        ),
                        TestRelativeDate.create(
                                null,
                                defaultSample,
                                null,
                                calPer,
                                archPerCalMethod,
                                "ArchPer version calendar"
                        ),
                        TestRelativeDate.create(
                                null,
                                defaultSample,
                                null,
                                radio,
                                geolPerRadioMethod,
                                "GeolPer version radiometric"
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
        return new TestEqualityHelper<>();
    }

    private static class RelativeDateComparator implements Comparator<RelativeDate> {
        @Override
        public int compare(RelativeDate o1, RelativeDate o2) {
            return o1.getNotes().compareTo(o2.getNotes());
        }
    }
}
