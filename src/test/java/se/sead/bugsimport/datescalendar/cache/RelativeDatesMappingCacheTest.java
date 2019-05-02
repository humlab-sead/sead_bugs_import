package se.sead.bugsimport.datescalendar.cache;

import org.junit.Before;
import org.junit.Test;
import se.sead.bugsimport.BugsListSeadMapping;
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
        BugsListSeadMapping<DatesCalendar, RelativeDate> mapping = mappingCreator.createMapping("SAMPLE", "From");
        mappingCache.add(mapping);
        assertFalse(mappingCache.getMergeablePeriodDates("SAMPLE").isEmpty());
        assertTrue(mappingCache.getNonMergeablePeriodDates("SAMPLE").isEmpty());
    }

    @Test
    public void addToIsStoredInMergeableList(){
        BugsListSeadMapping<DatesCalendar, RelativeDate> mapping = mappingCreator.createMapping("SAMPLE", "To");
        mappingCache.add(mapping);
        assertFalse(mappingCache.getMergeablePeriodDates("SAMPLE").isEmpty());
        assertTrue(mappingCache.getNonMergeablePeriodDates("SAMPLE").isEmpty());
    }

    @Test
    public void addFromCaIsStoredInMergeableList(){
        BugsListSeadMapping<DatesCalendar, RelativeDate> mapping = mappingCreator.createMapping("SAMPLE", "FromCa");
        mappingCache.add(mapping);
        assertFalse(mappingCache.getMergeablePeriodDates("SAMPLE").isEmpty());
        assertTrue(mappingCache.getNonMergeablePeriodDates("SAMPLE").isEmpty());
    }

    @Test
    public void addToCaIsStoredInMergeableList(){
        BugsListSeadMapping<DatesCalendar, RelativeDate> mapping = mappingCreator.createMapping("SAMPLE", "ToCa");
        mappingCache.add(mapping);
        assertFalse(mappingCache.getMergeablePeriodDates("SAMPLE").isEmpty());
        assertTrue(mappingCache.getNonMergeablePeriodDates("SAMPLE").isEmpty());
    }

    @Test
    public void addCaIsStoredInNonMergeableList(){
        BugsListSeadMapping<DatesCalendar, RelativeDate> mapping = mappingCreator.createMapping("SAMPLE", "Ca");
        mappingCache.add(mapping);
        assertFalse(mappingCache.getNonMergeablePeriodDates("SAMPLE").isEmpty());
        assertTrue(mappingCache.getMergeablePeriodDates("SAMPLE").isEmpty());
    }

    @Test
    public void addDifferentSamplesToDifferentLists(){
        BugsListSeadMapping<DatesCalendar, RelativeDate> from = mappingCreator.createMapping("Sample mergeable", "From");
        BugsListSeadMapping<DatesCalendar, RelativeDate> different = mappingCreator.createMapping("Sample nonmergeable", "Different");
        mappingCache.add(from);
        mappingCache.add(different);
        assertTrue(mappingCache.getMergeablePeriodDates("Sample nonmergeable").isEmpty());
        assertFalse(mappingCache.getMergeablePeriodDates("Sample mergeable").isEmpty());
        assertTrue(mappingCache.getNonMergeablePeriodDates("Sample mergeable").isEmpty());
        assertFalse(mappingCache.getNonMergeablePeriodDates("Sample nonmergeable").isEmpty());
    }

    @Test
    public void sampleNameExistsOnlyOnceRegardlessOfUncertainty(){
        BugsListSeadMapping<DatesCalendar, RelativeDate> from = mappingCreator.createMapping("Sample", "From");
        BugsListSeadMapping<DatesCalendar, RelativeDate> different = mappingCreator.createMapping("Sample", "Different");
        mappingCache.add(from);
        mappingCache.add(different);
        assertEquals(1, mappingCache.getStoredSampleCodes().size());
    }
}