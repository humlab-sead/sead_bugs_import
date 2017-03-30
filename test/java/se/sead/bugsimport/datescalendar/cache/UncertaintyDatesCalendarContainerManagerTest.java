package se.sead.bugsimport.datescalendar.cache;

import org.junit.Before;
import org.junit.Test;
import se.sead.bugsimport.MappingResult;
import se.sead.bugsimport.datescalendar.bugsmodel.DatesCalendar;
import se.sead.bugsimport.datesperiod.seadmodel.RelativeDate;
import se.sead.bugsimport.datesradio.seadmodel.DatingUncertainty;
import se.sead.repositories.DatingUncertaintyRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class UncertaintyDatesCalendarContainerManagerTest {

    private DatingUncertaintyManager datingUncertaintyManager;
    private UncertaintyDatesCalendarContainerManager ageMerger;
    private MappingCreator mappingCreator = new MappingCreator();

    @Before
    public void setup(){
        datingUncertaintyManager = new DatingUncertaintyManager(
            "From",
                "To",
                "Ca",
                "FromCa",
                "ToCa",
                new MockDatingUncertaintyRepository()
        );
        ageMerger = new UncertaintyDatesCalendarContainerManager(
                datingUncertaintyManager
        );
    }

    @Test
    public void sampleHasOneFromAndOneToRelativeDates(){
        List<MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate>> sampleData =
                Arrays.asList(
                        mappingCreator.createMapping("SAMPLE", "From"),
                        mappingCreator.createMapping("SAMPLE", "To")
                );
        List<UncertaintyDatesCalendarContainer> ranges = ageMerger.createRanges(sampleData);
        assertEquals("From", ranges.get(0).getFromDate().getUncertainty());
        assertEquals("To", ranges.get(0).getToDate().getUncertainty());
    }

    @Test
    public void orderOfUncertaintiesInListDoesNotMatter(){
        List<MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate>> sampleData =
                Arrays.asList(
                        mappingCreator.createMapping("SAMPLE", "To"),
                        mappingCreator.createMapping("SAMPLE", "From")
                );
        List<UncertaintyDatesCalendarContainer> ranges = ageMerger.createRanges(sampleData);
        assertEquals("From", ranges.get(0).getFromDate().getUncertainty());
        assertEquals("To", ranges.get(0).getToDate().getUncertainty());
    }

    @Test
    public void fromAndToUncertaintyIsCaUncertainties(){
        List<MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate>> sampleData =
                Arrays.asList(
                        mappingCreator.createMapping("SAMPLE", "FromCa"),
                        mappingCreator.createMapping("SAMPLE", "ToCa")
                );
        List<UncertaintyDatesCalendarContainer> ranges = ageMerger.createRanges(sampleData);
        assertEquals("FromCa", ranges.get(0).getFromDate().getUncertainty());
        assertEquals("ToCa", ranges.get(0).getToDate().getUncertainty());
    }

    @Test
    public void noFromOrToUncertainty(){
        List<MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate>> sampleData =
                Arrays.asList(
                        mappingCreator.createMapping("SAMPLE", "Unknown"),
                        mappingCreator.createMapping("SAMPLE", "Ca")
                );
        List<UncertaintyDatesCalendarContainer> ranges = ageMerger.createRanges(sampleData);
        assertTrue(ranges.isEmpty());
    }

    @Test
    public void moreThanFromAndToUncertaintyResultInFindingFromAndTo(){
        List<MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate>> sampleData =
                Arrays.asList(
                        mappingCreator.createMapping("SAMPLE", "Unknown"),
                        mappingCreator.createMapping("SAMPLE", "From"),
                        mappingCreator.createMapping("SAMPLE", "To")
                );
        List<UncertaintyDatesCalendarContainer> ranges = ageMerger.createRanges(sampleData);
        assertEquals(1, ranges.size());
        assertEquals("From", ranges.get(0).getFromDate().getUncertainty());
        assertEquals("To", ranges.get(0).getToDate().getUncertainty());
    }

    @Test(expected = IllegalStateException.class)
    public void moreThanOneFromIsError(){
        List<MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate>> sampleData =
                Arrays.asList(
                        mappingCreator.createMapping("SAMPLE", "From"),
                        mappingCreator.createMapping("SAMPLE", "From"),
                        mappingCreator.createMapping("SAMPLE", "To")
                );
        ageMerger.createRanges(sampleData);
    }

    @Test
    public void bothCaAndNonCaExists(){
        List<MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate>> sampleData =
                Arrays.asList(
                        mappingCreator.createMapping("SAMPLE", "FromCa"),
                        mappingCreator.createMapping("SAMPLE", "ToCa"),
                        mappingCreator.createMapping("SAMPLE", "From"),
                        mappingCreator.createMapping("SAMPLE", "To")
                );
        List<UncertaintyDatesCalendarContainer> ranges = ageMerger.createRanges(sampleData);
        assertEquals("FromCa", ranges.get(0).getFromDate().getUncertainty());
        assertEquals("ToCa", ranges.get(0).getToDate().getUncertainty());
        assertEquals("From", ranges.get(1).getFromDate().getUncertainty());
        assertEquals("To", ranges.get(1).getToDate().getUncertainty());
    }

    @Test
    public void bothCaAndNonCaExistsInAnyOrder(){
        List<MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate>> sampleData =
                Arrays.asList(
                        mappingCreator.createMapping("SAMPLE", "To"),
                        mappingCreator.createMapping("SAMPLE", "FromCa"),
                        mappingCreator.createMapping("SAMPLE", "From"),
                        mappingCreator.createMapping("SAMPLE", "ToCa")
                        );
        List<UncertaintyDatesCalendarContainer> ranges = ageMerger.createRanges(sampleData);
        assertEquals("FromCa", ranges.get(0).getFromDate().getUncertainty());
        assertEquals("ToCa", ranges.get(0).getToDate().getUncertainty());
        assertEquals("From", ranges.get(1).getFromDate().getUncertainty());
        assertEquals("To", ranges.get(1).getToDate().getUncertainty());
    }

    @Test
    public void openEndedFromIsAccepted(){
        List<MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate>> sampleData =
                Arrays.asList(
                        mappingCreator.createMapping("SAMPLE", "From")
                );
        List<UncertaintyDatesCalendarContainer> ranges = ageMerger.createRanges(sampleData);
        assertEquals("From", ranges.get(0).getFromDate().getUncertainty());
        assertNull(ranges.get(0).getToDate().getUncertainty());
    }

    @Test
    public void openEndedToIsAccepted(){
        List<MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate>> sampleData =
                Arrays.asList(
                        mappingCreator.createMapping("SAMPLE", "To")
                );
        List<UncertaintyDatesCalendarContainer> ranges = ageMerger.createRanges(sampleData);
        assertEquals("To", ranges.get(0).getToDate().getUncertainty());
        assertNull(ranges.get(0).getFromDate().getUncertainty());
    }

    @Test
    public void openEndedToAndFromCaIsAccepted(){
        List<MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate>> sampleData =
                Arrays.asList(
                        mappingCreator.createMapping("SAMPLE", "To"),
                        mappingCreator.createMapping("SAMPLE", "FromCa")
                );
        List<UncertaintyDatesCalendarContainer> ranges = ageMerger.createRanges(sampleData);
        assertEquals("FromCa", ranges.get(0).getFromDate().getUncertainty());
        assertNull(ranges.get(0).getToDate().getUncertainty());
        assertEquals("To", ranges.get(1).getToDate().getUncertainty());
        assertNull(ranges.get(1).getFromDate().getUncertainty());
    }

    private static class MockDatingUncertaintyRepository implements DatingUncertaintyRepository {

        @Override
        public DatingUncertainty findOne(Integer id) {
            return null;
        }

        @Override
        public DatingUncertainty findByName(String uncertaintySymbol) {
            DatingUncertainty uncertainty = new DatingUncertainty();
            uncertainty.setName(uncertaintySymbol);
            return uncertainty;
        }
    }

}