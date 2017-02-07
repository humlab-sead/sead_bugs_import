package se.sead.bugsimport.periods.converters;

import org.junit.Before;
import org.junit.Test;
import se.sead.bugsimport.locations.seadmodel.Location;
import se.sead.bugsimport.periods.PeriodCreator;
import se.sead.bugsimport.periods.bugsmodel.Period;
import se.sead.bugsimport.periods.converters.age.C14AgeConverter;
import se.sead.bugsimport.periods.converters.age.CalendarAgeConverter;
import se.sead.bugsimport.periods.converters.geography.GeographicExtentUpdater;
import se.sead.bugsimport.periods.seadmodel.RelativeAge;
import se.sead.bugsimport.periods.seadmodel.RelativeAgeType;
import se.sead.model.TestRelativeAge;
import se.sead.repositories.RelativeAgeTypeRepository;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static se.sead.bugsimport.periods.PeriodCreator.PeriodBuilder.*;

public class RelativeAgeUpdaterTest {

    private RelativeAgeUpdater updater;
    private GeographicExtentUpdater geographicExtentUpdater;
    private RelativeAgeTypeRepository relativeAgeTypeRepository;
    private C14AgeConverter c14Converter;
    private CalendarAgeConverter calendarAgeConverter;

    @Before
    public void setup(){
        c14Converter = new C14AgeConverter();
        calendarAgeConverter = new CalendarAgeConverter();
        geographicExtentUpdater = new MockGeographicExtentUpdater();
        relativeAgeTypeRepository = new MockRelativeAgeTypeRepository();
        updater = new RelativeAgeUpdater(
                geographicExtentUpdater,
                relativeAgeTypeRepository,
                c14Converter,
                calendarAgeConverter
        );
    }

    @Test
    public void updateNewItemStandardC14Period(){
        Period c14Period =
                PeriodCreator.createBuilder()
                .asC14()
                .done();
        RelativeAge relativeAge = new RelativeAge();
        updater.update(relativeAge, c14Period);
        assertEquals(DEFAULT_DESCRIPTION, relativeAge.getDescription());
        assertEquals(DEFAULT_PERIOD_CODE, relativeAge.getAbbreviation());
        assertEquals(DEFAULT_GEOGRAPHY, relativeAge.getGeographicExtent().getName());
        assertEquals(DEFAULT_TYPE, relativeAge.getType().getType());
        assertEquals(convertToSeadType(DEFAULT_AGE), relativeAge.getC14AgeOlder());
        assertEquals(convertToSeadType(DEFAULT_AGE), relativeAge.getC14AgeYounger());
        assertNull(relativeAge.getCalAgeOlder());
        assertNull(relativeAge.getCalAgeYounger());
        assertTrue(relativeAge.isNewItem());
        assertTrue(relativeAge.isUpdated());
        assertTrue(relativeAge.isErrorFree());
        assertFalse(relativeAge.isMarkedForDeletion());
    }

    private BigDecimal convertToSeadType(Integer ageValue){
        MathContext mathContext = new MathContext(20, RoundingMode.HALF_DOWN);
        BigDecimal value = new BigDecimal(ageValue, mathContext);
        value.setScale(5);
        return value;
    }

    @Test
    public void updateOldItemStandardC14Period(){
        Period period = PeriodCreator.createBuilder()
                .setBegin(1)
                .setEnd(1)
                .asC14()
                .done();
        RelativeAge relativeAge = createDefaultC14RelativeAge(0);
        updater.update(relativeAge, period);
        assertEquals(DEFAULT_DESCRIPTION, relativeAge.getDescription());
        assertEquals(DEFAULT_PERIOD_CODE, relativeAge.getAbbreviation());
        assertEquals(DEFAULT_GEOGRAPHY, relativeAge.getGeographicExtent().getName());
        assertEquals(DEFAULT_TYPE, relativeAge.getType().getType());
        assertEquals(convertToSeadType(DEFAULT_AGE), relativeAge.getC14AgeOlder());
        assertEquals(convertToSeadType(DEFAULT_AGE), relativeAge.getC14AgeYounger());
        assertNull(relativeAge.getCalAgeOlder());
        assertNull(relativeAge.getCalAgeYounger());
        assertFalse(relativeAge.isNewItem());
        assertTrue(relativeAge.isUpdated());
        assertTrue(relativeAge.isErrorFree());
        assertFalse(relativeAge.isMarkedForDeletion());
    }

    private RelativeAge createDefaultC14RelativeAge(Integer age){
        RelativeAge defaultRelativeAge = createDefaultRelativeAge();
        defaultRelativeAge.setC14AgeOlder(convertToSeadType(age));
        defaultRelativeAge.setC14AgeYounger(convertToSeadType(age));
        return defaultRelativeAge;
    }

    private RelativeAge createDefaultRelativeAge(){
        return TestRelativeAge
                .create(1,
                        DEFAULT_PERIOD_CODE,
                        DEFAULT_PERIOD_NAME,
                        null,
                        null,
                        null,
                        null,
                        DEFAULT_DESCRIPTION,
                        relativeAgeTypeRepository.findByType(DEFAULT_TYPE),
                        null);
    }

    @Test
    public void noLocationIsOk(){
        Period period = PeriodCreator.createBuilder()
                .asC14()
                .setGeography(null)
                .done();
        RelativeAge relativeAge = new RelativeAge();
        updater.update(relativeAge, period);
        assertNull(relativeAge.getGeographicExtent());
        assertTrue(relativeAge.isErrorFree());
    }

    @Test
    public void noUpdatesMade(){
        Period period = PeriodCreator.createBuilder()
                .asC14()
                .setGeography(null)
                .done();
        RelativeAge relativeAge = createDefaultC14RelativeAge(DEFAULT_AGE);
        updater.update(relativeAge, period);
        assertFalse(relativeAge.isNewItem());
        assertFalse(relativeAge.isUpdated());
    }

    @Test
    public void updateADCalendarPeriodFromNew(){
        Period period = PeriodCreator.createBuilder()
                .setBeginBCAD(AD_AGE_RELATION_SPECIFICATION)
                .setEndBCAD(AD_AGE_RELATION_SPECIFICATION)
                .asCalendar()
                .done();
        RelativeAge relativeAge = new RelativeAge();
        updater.update(relativeAge, period);
        assertEquals(DEFAULT_DESCRIPTION, relativeAge.getDescription());
        assertEquals(DEFAULT_GEOGRAPHY, relativeAge.getGeographicExtent().getName());
        assertEquals(DEFAULT_TYPE, relativeAge.getType().getType());
        assertEquals(DEFAULT_PERIOD_CODE, relativeAge.getAbbreviation());
        assertEquals(convertToSeadType(1949), relativeAge.getCalAgeYounger());
        assertEquals(convertToSeadType(1949), relativeAge.getCalAgeOlder());
        assertNull(relativeAge.getC14AgeOlder());
        assertNull(relativeAge.getC14AgeYounger());
        assertTrue(relativeAge.isErrorFree());
        assertTrue(relativeAge.isNewItem());
        assertTrue(relativeAge.isUpdated());
        assertFalse(relativeAge.isMarkedForDeletion());
    }

    private RelativeAge createDefaultCalendarRelativeAge(Integer begin, Integer end){
        RelativeAge defaultRelativeAge = createDefaultRelativeAge();
        defaultRelativeAge.setCalAgeOlder(convertToSeadType(begin));
        defaultRelativeAge.setCalAgeYounger(convertToSeadType(end));
        return defaultRelativeAge;
    }

    @Test
    public void updateADCalendarPeriodFromExisting(){
        Period period = PeriodCreator.createBuilder()
                .setBeginBCAD(AD_AGE_RELATION_SPECIFICATION)
                .setEndBCAD(AD_AGE_RELATION_SPECIFICATION)
                .asCalendar()
                .done();
        RelativeAge relativeAge = createDefaultCalendarRelativeAge(2,2);
        updater.update(relativeAge, period);
        assertEquals(DEFAULT_DESCRIPTION, relativeAge.getDescription());
        assertEquals(DEFAULT_GEOGRAPHY, relativeAge.getGeographicExtent().getName());
        assertEquals(DEFAULT_TYPE, relativeAge.getType().getType());
        assertEquals(DEFAULT_PERIOD_CODE, relativeAge.getAbbreviation());
        assertEquals(convertToSeadType(1949), relativeAge.getCalAgeYounger());
        assertEquals(convertToSeadType(1949), relativeAge.getCalAgeOlder());
        assertNull(relativeAge.getC14AgeOlder());
        assertNull(relativeAge.getC14AgeYounger());
        assertTrue(relativeAge.isErrorFree());
        assertFalse(relativeAge.isNewItem());
        assertTrue(relativeAge.isUpdated());
        assertFalse(relativeAge.isMarkedForDeletion());
    }

    private static class MockGeographicExtentUpdater extends GeographicExtentUpdater {

        private Location geographyLocation;

        public MockGeographicExtentUpdater() {
            super(Collections.EMPTY_LIST);
            setupGeography();
        }

        private void setupGeography() {
            geographyLocation = new Location();
            geographyLocation.setName(PeriodCreator.PeriodBuilder.DEFAULT_GEOGRAPHY);
        }

        void setGeographyName(String name){
            geographyLocation.setName(name);
        }

        @Override
        public Location getLocation(Period period) {
            if(geographyLocation.getName().equals(period.getGeography())){
                return geographyLocation;
            }
            return super.getLocation(period);
        }
    }

    private static class MockRelativeAgeTypeRepository implements RelativeAgeTypeRepository {

        private List<RelativeAgeType> types;

        MockRelativeAgeTypeRepository(){
            types = new ArrayList<>();
            int idCounter = 1;
            for (String type:
                 PeriodCreator.PeriodBuilder.TYPES) {
                types.add(create(idCounter++, type));
            }
        }

        static RelativeAgeType create(Integer id, String name){
            RelativeAgeType type = new RelativeAgeType();
            type.setId(id);
            type.setType(name);
            return type;
        }

        @Override
        public RelativeAgeType findOne(Integer id) {
            Optional<RelativeAgeType> first = types.stream().filter(type -> type.getId().equals(id)).findFirst();
            if(first.isPresent()){
                return first.get();
            }
            return null;
        }

        @Override
        public RelativeAgeType findByType(String typeName) {
            Optional<RelativeAgeType> first = types.stream().filter(type -> type.getType().equals(typeName)).findFirst();
            if(first.isPresent()){
                return first.get();
            }
            return null;
        }
    }
}