package se.sead.bugsimport.datescalendar.cache;

import org.junit.Before;
import org.junit.Test;
import se.sead.bugsimport.MappingResult;
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
        MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate> from = mappingCreator.createMapping("SAMPLE", "From");
        MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate> to = mappingCreator.createMapping("SAMPLE", "To");
        List<MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate>> mappings = new ArrayList<>(Arrays.asList(from, to));
        List<MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate>> result = dateMerger.mergeItems(mappings);
        assertEquals(1, result.size());
        assertEquals("SAMPLE-From-SAMPLE-To", result.get(0).getSeadData().get(0).getRelativeAge().getAbbreviation());
    }

    @Test
    public void mergeSeveralTypesOfRanges(){
        MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate> from = mappingCreator.createMapping("SAMPLE", "From");
        MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate> to = mappingCreator.createMapping("SAMPLE", "To");
        MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate> fromCa = mappingCreator.createMapping("SAMPLE", "FromCa");
        MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate> toCa = mappingCreator.createMapping("SAMPLE", "ToCa");
        List<MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate>> mappings = new ArrayList<>(Arrays.asList(from, to, fromCa, toCa));
        List<MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate>> result = dateMerger.mergeItems(mappings);
        assertEquals(2, result.size());
        assertEquals("SAMPLE-FromCa-SAMPLE-ToCa", result.get(0).getSeadData().get(0).getRelativeAge().getAbbreviation());
        assertEquals("SAMPLE-From-SAMPLE-To", result.get(1).getSeadData().get(0).getRelativeAge().getAbbreviation());
    }

    @Test
    public void mergeOpenEndedRange(){
        MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate> from = mappingCreator.createMapping("SAMPLE", "From");
        List<MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate>> mappings = new ArrayList<>(Arrays.asList(from));
        List<MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate>> result = dateMerger.mergeItems(mappings);
        assertEquals(1, result.size());
        assertEquals("SAMPLE-From-null-null", result.get(0).getSeadData().get(0).getRelativeAge().getAbbreviation());
    }

    @Test
    public void mergeMixedOpenEndedRange(){
        MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate> from = mappingCreator.createMapping("SAMPLE", "From");
        MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate> fromCa = mappingCreator.createMapping("SAMPLE", "FromCa");
        MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate> toCa = mappingCreator.createMapping("SAMPLE", "ToCa");
        List<MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate>> mappings = new ArrayList<>(Arrays.asList(from, fromCa, toCa));
        List<MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate>> result = dateMerger.mergeItems(mappings);
        assertEquals(2, result.size());
        assertEquals("SAMPLE-FromCa-SAMPLE-ToCa", result.get(0).getSeadData().get(0).getRelativeAge().getAbbreviation());
        assertEquals("SAMPLE-From-null-null", result.get(1).getSeadData().get(0).getRelativeAge().getAbbreviation());
    }

    @Test
    public void mergeFromAndToWithExtraCopyableData(){
        MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate> from = mappingCreator.createMapping("SAMPLE", "From");
        MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate> to = mappingCreator.createMapping("SAMPLE", "To");
        to.getSeadData().get(0).setNotes("Extra note information");
        List<MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate>> mappings = new ArrayList<>(Arrays.asList(from, to));
        List<MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate>> result = dateMerger.mergeItems(mappings);
        assertEquals(1, result.size());
        assertEquals("SAMPLE-From-SAMPLE-To", result.get(0).getSeadData().get(0).getRelativeAge().getAbbreviation());
        assertEquals("Extra note information", result.get(0).getSeadData().get(0).getNotes());
    }

    @Test
    public void openEndedContainNonCopiedNotes(){
        MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate> to = mappingCreator.createMapping("SAMPLE", "To");
        to.getSeadData().get(0).setNotes("Extra note information");
        List<MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate>> mappings = new ArrayList<>(Arrays.asList(to));
        List<MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate>> result = dateMerger.mergeItems(mappings);
        assertEquals(1, result.size());
        assertEquals("null-null-SAMPLE-To", result.get(0).getSeadData().get(0).getRelativeAge().getAbbreviation());
        assertEquals("Extra note information", result.get(0).getSeadData().get(0).getNotes());
    }

    @Test
    public void mergeCanUpdateUncertainty(){
        MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate> from = mappingCreator.createMapping("SAMPLE", "FromCa");
        MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate> to = mappingCreator.createMapping("SAMPLE", "ToCa");
        List<MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate>> mappings = new ArrayList<>(Arrays.asList(from, to));
        List<MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate>> result = dateMerger.mergeItems(mappings);
        assertEquals(1, result.size());
        assertEquals("SAMPLE-FromCa-SAMPLE-ToCa", result.get(0).getSeadData().get(0).getRelativeAge().getAbbreviation());
        assertEquals("Ca", result.get(0).getSeadData().get(0).getUncertainty().getName());
    }

    @Test
    public void mergeMixedOpenEndedRangeCopyMessageToCorrectPair(){
        MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate> from = mappingCreator.createMapping("SAMPLE", "From");
        MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate> fromCa = mappingCreator.createMapping("SAMPLE", "FromCa");
        MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate> toCa = mappingCreator.createMapping("SAMPLE", "ToCa");
        toCa.getSeadData().get(0).setNotes("Extra note");
        List<MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate>> mappings = new ArrayList<>(Arrays.asList(from, fromCa, toCa));
        List<MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate>> result = dateMerger.mergeItems(mappings);
        assertEquals(2, result.size());
        assertEquals("SAMPLE-FromCa-SAMPLE-ToCa", result.get(0).getSeadData().get(0).getRelativeAge().getAbbreviation());
        assertEquals("Extra note", result.get(0).getSeadData().get(0).getNotes());
        assertEquals("SAMPLE-From-null-null", result.get(1).getSeadData().get(0).getRelativeAge().getAbbreviation());
        assertNull(result.get(1).getSeadData().get(0).getNotes());
    }

    @Test
    public void moreThanOneUncertaintyOfSameTypeForSameSample(){
        MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate> fromCa = mappingCreator.createMapping("SAMPLE", "FromCa");
        MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate> fromCa2 = mappingCreator.createMapping("SAMPLE", "FromCa");
        MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate> toCa = mappingCreator.createMapping("SAMPLE", "ToCa");
        List<MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate>> mappings = new ArrayList<>(Arrays.asList(fromCa, fromCa2, toCa));
        List<MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate>> result = dateMerger.mergeItems(mappings);
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