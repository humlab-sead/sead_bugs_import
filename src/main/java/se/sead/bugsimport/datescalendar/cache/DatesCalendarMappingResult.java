package se.sead.bugsimport.datescalendar.cache;

import org.springframework.beans.factory.annotation.Autowired;
import se.sead.bugsimport.BugsListSeadMapping;
import se.sead.bugsimport.ListMappingResult;
import se.sead.bugsimport.datescalendar.bugsmodel.DatesCalendar;
import se.sead.bugsimport.datesperiod.seadmodel.RelativeDate;

import java.util.ArrayList;
import java.util.List;

public class DatesCalendarMappingResult extends ListMappingResult<DatesCalendar, RelativeDate> {

    private RelativeDateMerger dateMerger;
    private RelativeDatesMappingCache mappingCache;

    @Autowired
    public DatesCalendarMappingResult(
            DatingUncertaintyManager uncertaintyManager,
            RelativeDateMerger dateMerger) {
        mappingCache = new RelativeDatesMappingCache(uncertaintyManager);
        this.dateMerger = dateMerger;
    }

    @Override
    public void add(DatesCalendar bugsData, List<RelativeDate> seadItems) {
        BugsListSeadMapping<DatesCalendar, RelativeDate> mapping = new BugsListSeadMapping<>(bugsData, seadItems);
        mappingCache.add(mapping);
    }

    @Override
    public List<BugsListSeadMapping<DatesCalendar, RelativeDate>> getData() {
        List<BugsListSeadMapping<DatesCalendar, RelativeDate>> allMappings = new ArrayList<>();
        for (String sampleCode :
                mappingCache.getStoredSampleCodes()) {
            List<BugsListSeadMapping<DatesCalendar, RelativeDate>> mergeablePeriodDates = mappingCache.getMergeablePeriodDates(sampleCode);
            allMappings.addAll(dateMerger.mergeItems(mergeablePeriodDates));
            allMappings.addAll(mappingCache.getNonMergeablePeriodDates(sampleCode));
        }
        return allMappings;
    }
}
