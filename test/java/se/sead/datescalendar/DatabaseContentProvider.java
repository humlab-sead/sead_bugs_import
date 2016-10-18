package se.sead.datescalendar;

import se.sead.bugsimport.datesperiod.seadmodel.RelativeDate;
import se.sead.bugsimport.datesradio.seadmodel.DatingUncertainty;
import se.sead.bugsimport.periods.seadmodel.RelativeAge;
import se.sead.bugsimport.periods.seadmodel.RelativeAgeType;
import se.sead.bugsimport.sample.seadmodel.Sample;
import se.sead.bugsimport.site.seadmodel.SeadSite;
import se.sead.model.TestEqualityHelper;
import se.sead.model.TestRelativeAge;
import se.sead.model.TestRelativeDate;
import se.sead.repositories.*;
import se.sead.sead.methods.Method;
import se.sead.testutils.DatabaseContentVerification;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

class DatabaseContentProvider implements DatabaseContentVerification.DatabaseContentTestDataProvider<RelativeDate> {

    private static final MathContext SEAD_AGE_CONTEXT = new MathContext(20, RoundingMode.HALF_DOWN);
    private static final int SEAD_AGE_SCALE = 5;

    private RelativeDateRepository relativeDateRepository;
    private Sample defaultSample;
    private DatingUncertainty fromUncertainty;
    private RelativeAge cal100AD;
    private RelativeAge cal120AD;
    private Method archPer;
    private Method geolPer;
    private RelativeAgeType geologicalType;
    private RelativeAgeType archaeologicalType;

    DatabaseContentProvider(
            SampleRepository sampleRepository,
            DatingUncertaintyRepository datingUncertaintyRepository,
            MethodRepository methodRepository,
            RelativeAgeRepository relativeAgeRepository,
            RelativeAgeTypeRepository relativeAgeTypeRepository,
            RelativeDateRepository relativeDateRepository
    ){
        defaultSample = sampleRepository.findOne(1);
        fromUncertainty = datingUncertaintyRepository.findOne(3);
        cal100AD = relativeAgeRepository.findOne(1);
        cal120AD = relativeAgeRepository.findOne(2);
        archPer = methodRepository.findOne(3);
        geolPer = methodRepository.findOne(4);
        geologicalType = relativeAgeTypeRepository.findOne(1);
        archaeologicalType = relativeAgeTypeRepository.findOne(2);
        this.relativeDateRepository = relativeDateRepository;
    }

    @Override
    public List<RelativeDate> getExpectedData() {
        return Arrays.asList(
                TestRelativeDate.create(
                        1,
                        defaultSample,
                        fromUncertainty,
                        cal100AD,
                        archPer,
                        "Already stored"
                ),
                TestRelativeDate.create(
                        2,
                        defaultSample,
                        fromUncertainty,
                        cal120AD,
                        archPer,
                        "update change uncertainty"
                ),
                TestRelativeDate.create(
                        3,
                        defaultSample,
                        fromUncertainty,
                        cal100AD,
                        archPer,
                        "sead data changed since import"
                ),
                TestRelativeDate.create(
                        4,
                        defaultSample,
                        fromUncertainty,
                        cal120AD,
                        archPer,
                        "update change date"
                ),
                TestRelativeDate.create(
                        null,
                        defaultSample,
                        fromUncertainty,
                        cal120AD,
                        archPer,
                        "Insert"
                ),
                TestRelativeDate.create(
                        null,
                        defaultSample,
                        fromUncertainty,
                        cal100AD,
                        null,
                        "No method is ok -insert"
                ),
                TestRelativeDate.create(
                        null,
                        defaultSample,
                        null,
                        cal100AD,
                        archPer,
                        "No uncertainty is ok -insert"
                ),
                TestRelativeDate.create(
                        null,
                        defaultSample,
                        fromUncertainty,
                        createCalenderRelativeAge("CAL_" + 100 + "_BC", 2050, archaeologicalType),
                        archPer,
                        "insert bc version"
                ),
                TestRelativeDate.create(
                        null,
                        defaultSample,
                        fromUncertainty,
                        createC14RelativeAge(100, "BP", geologicalType),
                        geolPer,
                        "insert bp version"
                )
        );
    }

    private RelativeAge createCalenderRelativeAge(String abbrev, Integer date, RelativeAgeType type){
        BigDecimal calValue = createSeadValue(date);
        return TestRelativeAge.create(
                null,
                abbrev,
                null,
                null,
                null,
                calValue,
                calValue,
                "Autocreated from bugs import",
                type,
                null
        );
    }

    private BigDecimal createSeadValue(Integer date) {
        BigDecimal calValue = new BigDecimal(date, SEAD_AGE_CONTEXT);
        calValue.setScale(SEAD_AGE_SCALE);
        return calValue;
    }

    private RelativeAge createC14RelativeAge(Integer date, String bcadbp, RelativeAgeType type){
        BigDecimal c14Value = createSeadValue(date);
        return TestRelativeAge.create(
                null,
                "CAL_" + date + "_" + bcadbp,
                null,
                c14Value,
                c14Value,
                null,
                null,
                "Autocreated from bugs import",
                type,
                null
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
        TestEqualityHelper<RelativeDate> equalityHelper = new TestEqualityHelper<>(true);
        equalityHelper.addMethodInformation(new TestEqualityHelper.ClassMethodInformation(SeadSite.class, "getSiteLocations"));
        return equalityHelper;
    }

    private static class RelativeDateComparator implements Comparator<RelativeDate> {
        @Override
        public int compare(RelativeDate o1, RelativeDate o2) {
            return o1.getNotes().compareTo(o2.getNotes());
        }
    }
}
