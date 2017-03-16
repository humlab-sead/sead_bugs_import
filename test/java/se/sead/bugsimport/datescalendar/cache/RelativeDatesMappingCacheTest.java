package se.sead.bugsimport.datescalendar.cache;

import org.junit.Before;
import org.junit.Test;
import se.sead.bugsimport.MappingResult;
import se.sead.bugsimport.datescalendar.bugsmodel.DatesCalendar;
import se.sead.bugsimport.datesperiod.seadmodel.RelativeDate;

import static org.junit.Assert.*;

public class RelativeDatesMappingCacheTest {

    private RelativeDatesMappingCache mappingCache;
    private MappingCreator mappingCreator;

    @Before
    public void setup(){
        mappingCreator = new MappingCreator();
        DatingUncertaintyManager uncertaintyManager = new DatingUncertaintyManager(
                "From",
                "To",
                "Ca",
                "FromCa",
                "ToCa",
                new MockDatingUncertaintyRepository()
        );
        mappingCache = new RelativeDatesMappingCache(uncertaintyManager);
    }

    @Test
    public void addFromIsStoredInMergeableList(){
        MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate> mapping = mappingCreator.createMapping("SAMPLE", "From");
        mappingCache.add(mapping);
        assertFalse(mappingCache.getMergeablePeriodDates("SAMPLE").isEmpty());
        assertTrue(mappingCache.getNonMergeablePeriodDates("SAMPLE").isEmpty());
    }

    @Test
    public void addToIsStoredInMergeableList(){
        MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate> mapping = mappingCreator.createMapping("SAMPLE", "To");
        mappingCache.add(mapping);
        assertFalse(mappingCache.getMergeablePeriodDates("SAMPLE").isEmpty());
        assertTrue(mappingCache.getNonMergeablePeriodDates("SAMPLE").isEmpty());
    }

    @Test
    public void addFromCaIsStoredInMergeableList(){
        MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate> mapping = mappingCreator.createMapping("SAMPLE", "FromCa");
        mappingCache.add(mapping);
        assertFalse(mappingCache.getMergeablePeriodDates("SAMPLE").isEmpty());
        assertTrue(mappingCache.getNonMergeablePeriodDates("SAMPLE").isEmpty());
    }

    @Test
    public void addToCaIsStoredInMergeableList(){
        MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate> mapping = mappingCreator.createMapping("SAMPLE", "ToCa");
        mappingCache.add(mapping);
        assertFalse(mappingCache.getMergeablePeriodDates("SAMPLE").isEmpty());
        assertTrue(mappingCache.getNonMergeablePeriodDates("SAMPLE").isEmpty());
    }

    @Test
    public void addCaIsStoredInNonMergeableList(){
        MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate> mapping = mappingCreator.createMapping("SAMPLE", "Ca");
        mappingCache.add(mapping);
        assertFalse(mappingCache.getNonMergeablePeriodDates("SAMPLE").isEmpty());
        assertTrue(mappingCache.getMergeablePeriodDates("SAMPLE").isEmpty());
    }

    @Test
    public void addDifferentSamplesToDifferentLists(){
        MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate> from = mappingCreator.createMapping("Sample mergeable", "From");
        MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate> different = mappingCreator.createMapping("Sample nonmergeable", "Different");
        mappingCache.add(from);
        mappingCache.add(different);
        assertTrue(mappingCache.getMergeablePeriodDates("Sample nonmergeable").isEmpty());
        assertFalse(mappingCache.getMergeablePeriodDates("Sample mergeable").isEmpty());
        assertTrue(mappingCache.getNonMergeablePeriodDates("Sample mergeable").isEmpty());
        assertFalse(mappingCache.getNonMergeablePeriodDates("Sample nonmergeable").isEmpty());
    }

    @Test
    public void sampleNameExistsOnlyOnceRegardlessOfUncertainty(){
        MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate> from = mappingCreator.createMapping("Sample", "From");
        MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate> different = mappingCreator.createMapping("Sample", "Different");
        mappingCache.add(from);
        mappingCache.add(different);
        assertEquals(1, mappingCache.getStoredSampleCodes().size());
    }
}