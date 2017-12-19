package se.sead.bugsimport.datescalendar.cache.datepairs;

import org.junit.Before;
import org.junit.Test;
import se.sead.bugsimport.BugsListSeadMapping;
import se.sead.bugsimport.datescalendar.bugsmodel.DatesCalendar;
import se.sead.bugsimport.datesperiod.seadmodel.RelativeDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class DatingMethodExtractorTest {

    private DatingMethodExtractor extractor;

    @Before
    public void setup(){
        extractor = new DatingMethodExtractor();
    }

    @Test
    public void noDatingMethodsResultInDefaultGroup(){
        List<BugsListSeadMapping<DatesCalendar, RelativeDate>> sampleDates = createSampleDates(null, null);
        Map<String, List<BugsListSeadMapping<DatesCalendar, RelativeDate>>> stringListMap = extractor.extractFrom(sampleDates);
        assertEquals(1, stringListMap.size());
    }

    private List<BugsListSeadMapping<DatesCalendar, RelativeDate>> createSampleDates(String... datingMethods){
        List<BugsListSeadMapping<DatesCalendar, RelativeDate>> sampleDates = new ArrayList<>();
        for (String datingMethod :
                datingMethods) {
            sampleDates.add(new BugsListSeadMapping<DatesCalendar, RelativeDate>(createSampleDate(datingMethod), Collections.EMPTY_LIST));
        }
        return sampleDates;
    }

    private DatesCalendar createSampleDate(String datingMethod){
        DatesCalendar dc = new DatesCalendar();
        dc.setDatingMethod(datingMethod);
        return dc;
    }

    @Test
    public void groupsMatchesUniqueDatingMethods(){
        List<BugsListSeadMapping<DatesCalendar, RelativeDate>> sampleDates = createSampleDates("Method 1", "Method 1", "Method 2", null);
        Map<String, List<BugsListSeadMapping<DatesCalendar, RelativeDate>>> stringListMap = extractor.extractFrom(sampleDates);
        assertEquals(3, stringListMap.size());
        assertTrue(
                Stream.of("", "Method 1", "Method 2")
                .allMatch(s -> stringListMap.keySet().contains(s))
        );
    }

    @Test
    public void differentNumberOfItemsInDifferentGroups(){
        List<BugsListSeadMapping<DatesCalendar, RelativeDate>> sampleDates = createSampleDates("Method 1", "Method 1", "Method 2");
        Map<String, List<BugsListSeadMapping<DatesCalendar, RelativeDate>>> stringListMap = extractor.extractFrom(sampleDates);
        assertEquals(0, stringListMap.get("").size());
        assertEquals(1, stringListMap.get("Method 2").size());
        assertEquals(2, stringListMap.get("Method 1").size());
    }

}