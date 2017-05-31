package se.sead.bugsimport.datescalendar.cache;

import org.junit.Before;
import org.junit.Test;
import se.sead.bugsimport.BugsListSeadMapping;
import se.sead.bugsimport.datescalendar.bugsmodel.DatesCalendar;
import se.sead.bugsimport.datesperiod.seadmodel.RelativeDate;
import se.sead.bugsimport.periods.seadmodel.RelativeAge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class RelativeDateMergerTest {

    private RelativeDateMerger dateMerger;
    private UncertaintyDatesCalendarContainerManager ageMerger;
    private RelativeRangeAgeManager ageManager;
    private MappingCreator mappingCreator;

    @Before
    public void setup(){
        mappingCreator = new MappingCreator();
        ageMerger = new UncertaintyDatesCalendarContainerManager(new DatingUncertaintyManager(
                "From",
                "To",
                "Ca",
                "FromCa",
                "ToCa",
                new MockDatingUncertaintyRepository()
        ));
        ageManager = new MockRelativeRangeAgeManager();
        dateMerger = new RelativeDateMerger(ageMerger, ageManager);
    }

    @Test
    public void mergeFromAndTo(){
        BugsListSeadMapping<DatesCalendar, RelativeDate> from = mappingCreator.createMapping("SAMPLE", "From");
        BugsListSeadMapping<DatesCalendar, RelativeDate> to = mappingCreator.createMapping("SAMPLE", "To");
        List<BugsListSeadMapping<DatesCalendar, RelativeDate>> mappings = new ArrayList<>(Arrays.asList(from, to));
        List<BugsListSeadMapping<DatesCalendar, RelativeDate>> result = dateMerger.mergeItems(mappings);
        assertEquals(1, result.size());
        assertEquals("SAMPLE-From-SAMPLE-To", result.get(0).getSeadData().get(0).getRelativeAge().getAbbreviation());
    }

    @Test
    public void mergeSeveralTypesOfRanges(){
        BugsListSeadMapping<DatesCalendar, RelativeDate> from = mappingCreator.createMapping("SAMPLE", "From");
        BugsListSeadMapping<DatesCalendar, RelativeDate> to = mappingCreator.createMapping("SAMPLE", "To");
        BugsListSeadMapping<DatesCalendar, RelativeDate> fromCa = mappingCreator.createMapping("SAMPLE", "FromCa");
        BugsListSeadMapping<DatesCalendar, RelativeDate> toCa = mappingCreator.createMapping("SAMPLE", "ToCa");
        List<BugsListSeadMapping<DatesCalendar, RelativeDate>> mappings = new ArrayList<>(Arrays.asList(from, to, fromCa, toCa));
        List<BugsListSeadMapping<DatesCalendar, RelativeDate>> result = dateMerger.mergeItems(mappings);
        assertEquals(2, result.size());
        assertEquals("SAMPLE-FromCa-SAMPLE-ToCa", result.get(0).getSeadData().get(0).getRelativeAge().getAbbreviation());
        assertEquals("SAMPLE-From-SAMPLE-To", result.get(1).getSeadData().get(0).getRelativeAge().getAbbreviation());
    }

    @Test
    public void mergeOpenEndedRange(){
        BugsListSeadMapping<DatesCalendar, RelativeDate> from = mappingCreator.createMapping("SAMPLE", "From");
        List<BugsListSeadMapping<DatesCalendar, RelativeDate>> mappings = new ArrayList<>(Arrays.asList(from));
        List<BugsListSeadMapping<DatesCalendar, RelativeDate>> result = dateMerger.mergeItems(mappings);
        assertEquals(1, result.size());
        assertEquals("SAMPLE-From-null-null", result.get(0).getSeadData().get(0).getRelativeAge().getAbbreviation());
    }

    @Test
    public void mergeMixedOpenEndedRange(){
        BugsListSeadMapping<DatesCalendar, RelativeDate> from = mappingCreator.createMapping("SAMPLE", "From");
        BugsListSeadMapping<DatesCalendar, RelativeDate> fromCa = mappingCreator.createMapping("SAMPLE", "FromCa");
        BugsListSeadMapping<DatesCalendar, RelativeDate> toCa = mappingCreator.createMapping("SAMPLE", "ToCa");
        List<BugsListSeadMapping<DatesCalendar, RelativeDate>> mappings = new ArrayList<>(Arrays.asList(from, fromCa, toCa));
        List<BugsListSeadMapping<DatesCalendar, RelativeDate>> result = dateMerger.mergeItems(mappings);
        assertEquals(2, result.size());
        assertEquals("SAMPLE-FromCa-SAMPLE-ToCa", result.get(0).getSeadData().get(0).getRelativeAge().getAbbreviation());
        assertEquals("SAMPLE-From-null-null", result.get(1).getSeadData().get(0).getRelativeAge().getAbbreviation());
    }

    @Test
    public void mergeFromAndToWithExtraCopyableData(){
        BugsListSeadMapping<DatesCalendar, RelativeDate> from = mappingCreator.createMapping("SAMPLE", "From");
        BugsListSeadMapping<DatesCalendar, RelativeDate> to = mappingCreator.createMapping("SAMPLE", "To");
        to.getSeadData().get(0).setNotes("Extra note information");
        List<BugsListSeadMapping<DatesCalendar, RelativeDate>> mappings = new ArrayList<>(Arrays.asList(from, to));
        List<BugsListSeadMapping<DatesCalendar, RelativeDate>> result = dateMerger.mergeItems(mappings);
        assertEquals(1, result.size());
        assertEquals("SAMPLE-From-SAMPLE-To", result.get(0).getSeadData().get(0).getRelativeAge().getAbbreviation());
        assertEquals("Extra note information", result.get(0).getSeadData().get(0).getNotes());
    }

    @Test
    public void openEndedContainNonCopiedNotes(){
        BugsListSeadMapping<DatesCalendar, RelativeDate> to = mappingCreator.createMapping("SAMPLE", "To");
        to.getSeadData().get(0).setNotes("Extra note information");
        List<BugsListSeadMapping<DatesCalendar, RelativeDate>> mappings = new ArrayList<>(Arrays.asList(to));
        List<BugsListSeadMapping<DatesCalendar, RelativeDate>> result = dateMerger.mergeItems(mappings);
        assertEquals(1, result.size());
        assertEquals("null-null-SAMPLE-To", result.get(0).getSeadData().get(0).getRelativeAge().getAbbreviation());
        assertEquals("Extra note information", result.get(0).getSeadData().get(0).getNotes());
    }

    @Test
    public void mergeCanUpdateUncertainty(){
        BugsListSeadMapping<DatesCalendar, RelativeDate> from = mappingCreator.createMapping("SAMPLE", "FromCa");
        BugsListSeadMapping<DatesCalendar, RelativeDate> to = mappingCreator.createMapping("SAMPLE", "ToCa");
        List<BugsListSeadMapping<DatesCalendar, RelativeDate>> mappings = new ArrayList<>(Arrays.asList(from, to));
        List<BugsListSeadMapping<DatesCalendar, RelativeDate>> result = dateMerger.mergeItems(mappings);
        assertEquals(1, result.size());
        assertEquals("SAMPLE-FromCa-SAMPLE-ToCa", result.get(0).getSeadData().get(0).getRelativeAge().getAbbreviation());
        assertEquals("Ca", result.get(0).getSeadData().get(0).getUncertainty().getName());
    }

    @Test
    public void mergeMixedOpenEndedRangeCopyMessageToCorrectPair(){
        BugsListSeadMapping<DatesCalendar, RelativeDate> from = mappingCreator.createMapping("SAMPLE", "From");
        BugsListSeadMapping<DatesCalendar, RelativeDate> fromCa = mappingCreator.createMapping("SAMPLE", "FromCa");
        BugsListSeadMapping<DatesCalendar, RelativeDate> toCa = mappingCreator.createMapping("SAMPLE", "ToCa");
        toCa.getSeadData().get(0).setNotes("Extra note");
        List<BugsListSeadMapping<DatesCalendar, RelativeDate>> mappings = new ArrayList<>(Arrays.asList(from, fromCa, toCa));
        List<BugsListSeadMapping<DatesCalendar, RelativeDate>> result = dateMerger.mergeItems(mappings);
        assertEquals(2, result.size());
        assertEquals("SAMPLE-FromCa-SAMPLE-ToCa", result.get(0).getSeadData().get(0).getRelativeAge().getAbbreviation());
        assertEquals("Extra note", result.get(0).getSeadData().get(0).getNotes());
        assertEquals("SAMPLE-From-null-null", result.get(1).getSeadData().get(0).getRelativeAge().getAbbreviation());
        assertNull(result.get(1).getSeadData().get(0).getNotes());
    }

    @Test
    public void moreThanOneUncertaintyOfSameTypeForSameSample(){
        BugsListSeadMapping<DatesCalendar, RelativeDate> fromCa = mappingCreator.createMapping("SAMPLE", "FromCa");
        BugsListSeadMapping<DatesCalendar, RelativeDate> fromCa2 = mappingCreator.createMapping("SAMPLE", "FromCa");
        BugsListSeadMapping<DatesCalendar, RelativeDate> toCa = mappingCreator.createMapping("SAMPLE", "ToCa");
        List<BugsListSeadMapping<DatesCalendar, RelativeDate>> mappings = new ArrayList<>(Arrays.asList(fromCa, fromCa2, toCa));
        List<BugsListSeadMapping<DatesCalendar, RelativeDate>> result = dateMerger.mergeItems(mappings);
        assertEquals(1, result.size());
        assertFalse(result.get(0).isErrorFree());
    }

    private static class MockRelativeRangeAgeManager extends RelativeRangeAgeManager {

        public MockRelativeRangeAgeManager() {
            super(null, null, null, null);
        }

        @Override
        public RelativeAge createOrGet(DatesCalendar fromData, DatesCalendar toData) {
            RelativeAge relativeAge = new RelativeAge();
            relativeAge.setAbbreviation(buildAbbreviation(fromData, toData));
            return relativeAge;
        }

        private String buildAbbreviation(DatesCalendar from, DatesCalendar to){
            return
                    from.getSample() + "-" + from.getUncertainty() + "-" +
                    to.getSample() + "-" + to.getUncertainty();
        }
    }
}