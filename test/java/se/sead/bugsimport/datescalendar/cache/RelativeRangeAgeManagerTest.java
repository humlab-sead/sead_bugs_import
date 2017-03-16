package se.sead.bugsimport.datescalendar.cache;

import org.junit.Before;
import org.junit.Test;
import se.sead.bugsimport.datescalendar.bugsmodel.DatesCalendar;
import se.sead.bugsimport.datescalendar.converters.RelativeAgeTypeManager;
import se.sead.bugsimport.periods.bugsmodel.Period;
import se.sead.bugsimport.periods.converters.RelativeAgeCache;
import se.sead.bugsimport.periods.converters.RelativeAgeUpdater;
import se.sead.bugsimport.periods.seadmodel.RelativeAge;
import se.sead.repositories.RelativeAgeRepository;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class RelativeRangeAgeManagerTest {

    private RelativeAgeRepository ageRepository;
    private MockRelativeAgeUpdater ageUpdater;
    private RelativeAgeTypeManager ageTypeManager;
    private RelativeRangeAgeManager rangeAgeManager;
    private RelativeAgeCache ageCache;

    @Before
    public void setup(){
        ageUpdater = new MockRelativeAgeUpdater();
        ageTypeManager = new RelativeAgeTypeManager(
                "Calendar date range",
                "Calendar date"
        );
        ageCache = new RelativeAgeCache();
        ageRepository = new MockRelativeAgeRepository(ageCache);
        rangeAgeManager = new RelativeRangeAgeManager(
                ageRepository,
                ageUpdater,
                ageTypeManager,
                ageCache
        );
    }

    @Test
    public void mergeTwoDates(){
        DatesCalendar from = createDatesCalendar(100, "AD");
        DatesCalendar to = createDatesCalendar(200, "AD");
        RelativeAge relativeAge = rangeAgeManager.createOrGet(from, to);
        assertEquals("CAL_100-200_AD", ageUpdater.createdPeriod.getPeriodCode());
    }

    private DatesCalendar createDatesCalendar(Integer age, String bcadbp){
        DatesCalendar dc = new DatesCalendar();
        dc.setBcadbp(bcadbp);
        dc.setDate(age);
        return dc;
    }

    @Test
    public void mergeTwoDatesDifferentBCAD(){
        DatesCalendar bc = createDatesCalendar(100, "BC");
        DatesCalendar ad = createDatesCalendar(100, "AD");
        RelativeAge orGet = rangeAgeManager.createOrGet(bc, ad);
        assertEquals("CAL_100_BC-100_AD", ageUpdater.createdPeriod.getPeriodCode());
    }

    @Test
    public void mergeOpenEndedStart(){
        DatesCalendar to = createDatesCalendar(200, "AD");
        rangeAgeManager.createOrGet(new DatesCalendar(), to);
        assertEquals("CAL_-200_AD", ageUpdater.createdPeriod.getPeriodCode());
        assertEquals(Integer.valueOf(200), ageUpdater.createdPeriod.getEnd());
        assertEquals("AD", ageUpdater.createdPeriod.getEndBCad());
        assertNull(ageUpdater.createdPeriod.getBegin());
        assertNull(ageUpdater.createdPeriod.getBeginBCad());
    }

    @Test
    public void mergeOpenEndedStop(){
        DatesCalendar from = createDatesCalendar(200, "BC");
        rangeAgeManager.createOrGet(from, new DatesCalendar());
        assertEquals("CAL_200_BC-", ageUpdater.createdPeriod.getPeriodCode());
        assertEquals(Integer.valueOf(200), ageUpdater.createdPeriod.getBegin());
        assertEquals("BC", ageUpdater.createdPeriod.getBeginBCad());
        assertNull(ageUpdater.createdPeriod.getEnd());
        assertNull(ageUpdater.createdPeriod.getEndBCad());
    }

    private static class MockRelativeAgeUpdater extends RelativeAgeUpdater {

        Period createdPeriod;

        public MockRelativeAgeUpdater() {
            super(null, null, null, null);
        }

        @Override
        public void update(RelativeAge original, Period bugsData) {
            this.createdPeriod = bugsData;
        }
    }

    private static class MockRelativeAgeRepository implements RelativeAgeRepository {

        private RelativeAgeCache cache;

        MockRelativeAgeRepository(RelativeAgeCache cache){
            this.cache = cache;
        }

        @Override
        public List<RelativeAge> findAll() {
            return null;
        }

        @Override
        public RelativeAge findByAbbreviation(String abbreviation) {
            return cache.get(abbreviation);
        }

        @Override
        public RelativeAge findOne(Integer integer) {
            return null;
        }

        @Override
        public RelativeAge saveOrUpdate(RelativeAge entity) {
            return null;
        }
    }
}