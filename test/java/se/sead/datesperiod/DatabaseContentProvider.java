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
    private Method geolPerMethod;
    private Method archPerMethod;
    private RelativeAge existingAge;

    public DatabaseContentProvider(
            SampleRepository sampleRepository,
            DatingUncertaintyRepository datingUncertaintyRepository,
            MethodRepository methodRepository,
            RelativeAgeRepository relativeAgeRepository,
            RelativeDateRepository relativeDateRepository
    ){
        defaultSample = sampleRepository.findOne(1);
        greaterThanUncertainty = datingUncertaintyRepository.findOne(1);
        geolPerMethod = methodRepository.findOne(4);
        archPerMethod = methodRepository.findOne(3);
        existingAge = relativeAgeRepository.findOne(1);
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
                                geolPerMethod,
                                "Already stored"
                        ),
                        TestRelativeDate.create(
                                2,
                                defaultSample,
                                null,
                                existingAge,
                                geolPerMethod,
                                "Update"
                        ),
                        TestRelativeDate.create(
                                3,
                                defaultSample,
                                greaterThanUncertainty,
                                existingAge,
                                archPerMethod,
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
                                geolPerMethod,
                                "uncertainty is null, is ok -insert"
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
