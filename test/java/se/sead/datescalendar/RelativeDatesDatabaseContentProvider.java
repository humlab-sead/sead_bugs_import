package se.sead.datescalendar;

import se.sead.bugsimport.datesperiod.seadmodel.RelativeDate;
import se.sead.bugsimport.datesradio.seadmodel.DatingUncertainty;
import se.sead.bugsimport.periods.seadmodel.RelativeAge;
import se.sead.bugsimport.periods.seadmodel.RelativeAgeType;
import se.sead.bugsimport.sample.seadmodel.Sample;
import se.sead.bugsimport.site.seadmodel.SeadSite;
import se.sead.model.*;
import se.sead.repositories.*;
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

class RelativeDatesDatabaseContentProvider implements DatabaseContentVerification.DatabaseContentTestDataProvider<RelativeDate> {

    static final MathContext SEAD_AGE_CONTEXT = new MathContext(20, RoundingMode.HALF_DOWN);
    static final int SEAD_AGE_SCALE = 5;

    private RelativeDateRepository relativeDateRepository;
    private Sample defaultSample;
    private Sample sample2;
    private Sample sample3;
    private Sample sample5;
    private DatingUncertainty fromUncertainty;
    private DatingUncertainty caUncertainty;
    private DatingUncertainty toUncertainty;
    private RelativeAge cal100AD;
    private RelativeAge cal120AD;
    private Method archPer;
    private Method geolPer;
    private Method histCal;
    private Method unknownCal;
    private DataType calendarDate;
    private RelativeAgeType calendarDateType;
    private RelativeAgeType calendarDateRangeType;
    private DatasetMaster bugsMaster;

    RelativeDatesDatabaseContentProvider(
            SampleRepository sampleRepository,
            DatingUncertaintyRepository datingUncertaintyRepository,
            MethodRepository methodRepository,
            RelativeAgeRepository relativeAgeRepository,
            RelativeAgeTypeRepository relativeAgeTypeRepository,
            RelativeDateRepository relativeDateRepository,
            DatasetMasterRepository datasetMasterRepository,
            DataTypeRepository dataTypeRepository
    ){
        defaultSample = sampleRepository.findOne(1);
        sample2 = sampleRepository.findOne(2);
        sample3 = sampleRepository.findOne(3);
        sample5 = sampleRepository.findOne(5);
        fromUncertainty = datingUncertaintyRepository.findOne(3);
        caUncertainty = datingUncertaintyRepository.findOne(5);
        toUncertainty = datingUncertaintyRepository.findOne(4);
        cal100AD = relativeAgeRepository.findOne(1);
        cal120AD = relativeAgeRepository.findOne(2);
        archPer = methodRepository.findOne(3);
        geolPer = methodRepository.findOne(4);
        histCal = methodRepository.findOne(5);
        unknownCal = methodRepository.findOne(6);
        calendarDateType = relativeAgeTypeRepository.findOne(2);
        calendarDateRangeType = relativeAgeTypeRepository.findOne(3);
        this.relativeDateRepository = relativeDateRepository;
        bugsMaster = datasetMasterRepository.findBugsMasterSet();
        calendarDate = dataTypeRepository.findOne(1);
    }

    @Override
    public List<RelativeDate> getExpectedData() {
        return Arrays.asList(
                TestRelativeDate.create(
                        1,
                        null,
                        cal100AD,
                        "Already stored",
                        TestAnalysisEntity.create(
                                1,
                                TestDataset.create(1, "CALE000015", archPer,bugsMaster, calendarDate),
                                defaultSample
                        )
                ),
                TestRelativeDate.create(
                        2,
                        null,
                        cal120AD,
                        "update change uncertainty",
                        TestAnalysisEntity.create(
                                2,
                                TestDataset.create(2,"CALE000011", archPer, bugsMaster, calendarDate),
                                defaultSample
                        )
                ),
                TestRelativeDate.create(
                        3,
                        fromUncertainty,
                        cal100AD,
                        "sead data changed since import",
                        TestAnalysisEntity.create(
                                3,
                                TestDataset.create(3, "CALE000012", archPer, bugsMaster, calendarDate),
                                defaultSample
                        )
                ),
                TestRelativeDate.create(
                        4,
                        null,
                        cal120AD,
                        "update change date",
                        TestAnalysisEntity.create(
                                4,
                                TestDataset.create(4, "CALE000016", archPer, bugsMaster, calendarDate),
                                defaultSample
                        )

                ),
                TestRelativeDate.create(
                        null,
                        null,
                        cal120AD,
                        "Insert",
                        TestAnalysisEntity.create(
                                null,
                                TestDataset.create(null, "CALE000010", archPer, bugsMaster, calendarDate),
                                defaultSample
                        )
                ),
                TestRelativeDate.create(
                        null,
                        null,
                        cal100AD,
                        "No method is ok -insert",
                        TestAnalysisEntity.create(
                                null,
                                TestDataset.create(null, "CALE000013", unknownCal, bugsMaster, calendarDate),
                                defaultSample
                        )
                ),
                TestRelativeDate.create(
                        null,
                        null,
                        cal100AD,
                        "No uncertainty is ok -insert",
                        TestAnalysisEntity.create(
                                null,
                                TestDataset.create(null, "CALE000014", archPer, bugsMaster, calendarDate),
                                defaultSample
                        )
                ),
                TestRelativeDate.create(
                        null,
                        null,
                        createCalenderRelativeAge("CAL_" + 100 + "_BC", 2050, calendarDateType),
                        "insert bc version",
                        TestAnalysisEntity.create(
                                null,
                                TestDataset.create(null, "CALE000017", archPer, bugsMaster, calendarDate),
                                defaultSample
                        )
                ),
                TestRelativeDate.create(
                        null,
                        null,
                        createC14RelativeAge(100, "BP", calendarDateType),
                        "insert bp version",
                        TestAnalysisEntity.create(
                                null,
                                TestDataset.create(null, "CALE000018", geolPer, bugsMaster, calendarDate),
                                defaultSample
                        )
                ),
                TestRelativeDate.create(
                        null,
                        null,
                        createCalendarRelativeAge("CAL_" + 100 + "-" + 200 + "_AD", 1850, 1750, calendarDateRangeType),
                        "range",
                        TestAnalysisEntity.create(
                                null,
                                TestDataset.create(null, "CALE000019", archPer, bugsMaster, calendarDate),
                                sample2
                        )
                ),
                TestRelativeDate.create(
                        null,
                        caUncertainty,
                        createCalendarRelativeAge("CAL_" + 100 + "-" + 200 + "_AD", 1850, 1750, calendarDateRangeType),
                        "existing calendar range but with uncertainty",
                        TestAnalysisEntity.create(
                                null,
                                TestDataset.create(null, "CALE000021", archPer, bugsMaster, calendarDate),
                                sample3
                        )
                ),
                TestRelativeDate.create(
                        null,
                        fromUncertainty,
                        createCalendarRelativeAge("CAL_" + 100 + "_AD-", 1850, null, calendarDateRangeType),
                        "Add with open-ended period code",
                        TestAnalysisEntity.create(
                                null,
                                TestDataset.create(null, "CALE000023", archPer, bugsMaster, calendarDate),
                                sample3
                        )
                ),
                TestRelativeDate.create(
                        null,
                        toUncertainty,
                        createCalendarRelativeAge("CAL_-" + 200 + "_AD", null, 1750, calendarDateRangeType),
                        "Add with open-started period code",
                        TestAnalysisEntity.create(
                                null,
                                TestDataset.create(null, "CALE000024", archPer, bugsMaster, calendarDate),
                                defaultSample
                        )
                ),
                TestRelativeDate.create(
                        null,
                        null,
                        cal100AD,
                        "A method from another group",
                        TestAnalysisEntity.create(
                                null,
                                TestDataset.create(null, "CALE000025", histCal, bugsMaster, calendarDate),
                                sample3
                        )
                ),
                TestRelativeDate.create(
                        null,
                        null,
                        createCalendarRelativeAge("CAL_100-120_AD", 1850, 1830, calendarDateRangeType),
                        "C14 Date 1",
                        TestAnalysisEntity.create(
                                null,
                                TestDataset.create(null, "CALE000029", archPer, bugsMaster, calendarDate),
                                sample5
                        )
                ),
                TestRelativeDate.create(
                        null,
                        null,
                        createCalendarRelativeAge("CAL_90-120_AD", 1860, 1830, calendarDateRangeType),
                        "C14 Date 2",
                        TestAnalysisEntity.create(
                                null,
                                TestDataset.create(null, "CALE000031", archPer, bugsMaster, calendarDate),
                                sample5
                        )
                ),
                TestRelativeDate.create(
                        null,
                        null,
                        createCalendarRelativeAge("CAL_90-100_AD", 1860, 1850, calendarDateRangeType),
                        "Matched by note only",
                        TestAnalysisEntity.create(
                                null,
                                TestDataset.create(null, "CALE000033", archPer, bugsMaster, calendarDate),
                                sample5
                        )
                ),
                TestRelativeDate.create(
                        null,
                        null,
                        createCalendarRelativeAge("CAL_90-100_AD", 1860, 1850, calendarDateRangeType),
                        "Matched by note only",
                        TestAnalysisEntity.create(
                                null,
                                TestDataset.create(null, "CALE000033", archPer, bugsMaster, calendarDate),
                                sample5
                        )
                ),
                TestRelativeDate.create(
                        null,
                        fromUncertainty,
                        createCalendarRelativeAge("CAL_" + 100 + "_AD-", 1850, null, calendarDateRangeType),
                        "Open ended by something else",
                        TestAnalysisEntity.create(
                                null,
                                TestDataset.create(null, "CALE000035", archPer, bugsMaster, calendarDate),
                                sample5
                        )
                ),
                TestRelativeDate.create(
                        null,
                        toUncertainty,
                        createCalendarRelativeAge("CAL_-" + 100 + "_AD", null, 1850, calendarDateRangeType),
                        "Open ended start by something different",
                        TestAnalysisEntity.create(
                                null,
                                TestDataset.create(null, "CALE000036", archPer, bugsMaster, calendarDate),
                                defaultSample
                        )
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

    static BigDecimal createSeadValue(Integer date) {
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
                null,
                null,
                c14Value,
                c14Value,
                "Autocreated from bugs import",
                type,
                null
        );
    }

    private RelativeAge createCalendarRelativeAge(String abbrev, Integer start, Integer stop, RelativeAgeType type){
        BigDecimal startValue = start != null ? createSeadValue(start) : null;
        BigDecimal stopValue = stop != null ? createSeadValue(stop) : null;
        return TestRelativeAge.create(
                null,
                abbrev,
                null,
                null,
                null,
                startValue,
                stopValue,
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
        equalityHelper.addMethodInformation(new TestEqualityHelper.ClassMethodInformation(Dataset.class, "getContacts"));
        return equalityHelper;
    }

    private static class RelativeDateComparator implements Comparator<RelativeDate> {
        @Override
        public int compare(RelativeDate o1, RelativeDate o2) {
            String o1Notes = o1.getNotes();
            String o2Notes = o2.getNotes();
            if(o1.getNotes() == null){
                o1Notes = "";
            }
            if(o2.getNotes() == null){
                o2Notes = "";
            }
            return o1Notes.compareTo(o2Notes);
        }
    }
}
