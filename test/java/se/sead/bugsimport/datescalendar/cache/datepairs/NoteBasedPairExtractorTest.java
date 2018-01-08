package se.sead.bugsimport.datescalendar.cache.datepairs;

import org.junit.Before;
import org.junit.Test;
import se.sead.bugsimport.BugsListSeadMapping;
import se.sead.bugsimport.datescalendar.bugsmodel.DatesCalendar;
import se.sead.bugsimport.datesperiod.seadmodel.RelativeDate;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class NoteBasedPairExtractorTest {

    private NoteBasedPairExtractor pairExtractor;

    @Before
    public void setup(){
        pairExtractor = new NoteBasedPairExtractor();
    }

    @Test
    public void noNotesMeansAllInSameMappingList(){
        List<BugsListSeadMapping<DatesCalendar, RelativeDate>> sampleDates = createSampleDates(null, null);
        Map<String, List<BugsListSeadMapping<DatesCalendar, RelativeDate>>> stringListMap = pairExtractor.extractMappedPairs(sampleDates);
        assertEquals(1, stringListMap.size());
    }

    private List<BugsListSeadMapping<DatesCalendar, RelativeDate>> createSampleDates(String... bugsNotes){
        List<BugsListSeadMapping<DatesCalendar, RelativeDate>> sampleDates = new ArrayList<>();
        for (String note :
                bugsNotes) {
            sampleDates.add(new BugsListSeadMapping<DatesCalendar, RelativeDate>(createSampleDate(note), Collections.EMPTY_LIST));
        }
        return sampleDates;
    }

    private DatesCalendar createSampleDate(String note){
        DatesCalendar dc = new DatesCalendar();
        dc.setNotes(note);
        return dc;
    }

    @Test
    public void allNotesWithSameContentMeansSameGroup(){
        List<BugsListSeadMapping<DatesCalendar, RelativeDate>> sampleDates = createSampleDates("Date 1", "Date 1", "Date 1");
        Map<String, List<BugsListSeadMapping<DatesCalendar, RelativeDate>>> stringListMap = pairExtractor.extractMappedPairs(sampleDates);
        assertEquals("Date 1", String.join("", stringListMap.keySet()));
        assertEquals(3, stringListMap.get("Date 1").size());
    }

    @Test
    public void notesFromTwoSetsMeansTwoGroups(){
        List<BugsListSeadMapping<DatesCalendar, RelativeDate>> sampleDates = createSampleDates("Date 1", "Date 2");
        Map<String, List<BugsListSeadMapping<DatesCalendar, RelativeDate>>> stringListMap = pairExtractor.extractMappedPairs(sampleDates);
        assertTrue(
                Stream.of("Date 1", "Date 2")
                .allMatch(s -> stringListMap.containsKey(s))
        );
    }

    @Test
    public void notesFromOneSetAndVariousOtherNotesProducesDifferentGroups(){
        List<BugsListSeadMapping<DatesCalendar, RelativeDate>> sampleDates = createSampleDates("Date 1", "Blaha", "Other", "Groups");
        Map<String, List<BugsListSeadMapping<DatesCalendar, RelativeDate>>> stringListMap = pairExtractor.extractMappedPairs(sampleDates);
        assertEquals(4, stringListMap.size());
        assertTrue(
                Stream.of("Date 1", "Blaha", "Other", "Groups")
                .allMatch(s -> stringListMap.keySet().contains(s))
        );
    }

    @Test
    public void notesEndingWithMarkerProducesSeparateSets(){
        List<BugsListSeadMapping<DatesCalendar, RelativeDate>> sampleDates =
                createSampleDates("Data Date 1", "Data Date 1", "Data other", "Data free");
        Map<String, List<BugsListSeadMapping<DatesCalendar, RelativeDate>>> stringListMap = pairExtractor.extractMappedPairs(sampleDates);
        assertEquals(3, stringListMap.size());
        assertTrue(
                Stream.of("Data Date 1", "Data other", "Data free")
                .allMatch(s -> stringListMap.keySet().contains(s))
        );
    }
}