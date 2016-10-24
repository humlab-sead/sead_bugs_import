package se.sead.bugsimport.datescalendar.cache;

import se.sead.bugsimport.MappingResult;
import se.sead.bugsimport.datescalendar.bugsmodel.DatesCalendar;
import se.sead.bugsimport.datesperiod.seadmodel.RelativeDate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatesCalendarMappingResult extends MappingResult<DatesCalendar, RelativeDate> {

    private DatingUncertaintyManager uncertaintyManager;
    private RelativeAgeMerger relativeAgeMerger;

    private Map<String, List<BugsListSeadMapping<DatesCalendar, RelativeDate>>> datesBySample;

    public DatesCalendarMappingResult(
            DatingUncertaintyManager uncertaintyManager,
            RelativeAgeMerger relativeAgeMerger) {
        this.uncertaintyManager = uncertaintyManager;
        this.relativeAgeMerger = relativeAgeMerger;
        datesBySample = new HashMap<>();
    }

    @Override
    public void add(DatesCalendar bugsData, List<RelativeDate> seadItems) {
        BugsListSeadMapping<DatesCalendar, RelativeDate> mapping = new BugsListSeadMapping<>(bugsData, seadItems);
        DatesCalendarRangeMerger rangeMerger = new DatesCalendarRangeMerger(
                uncertaintyManager,
                relativeAgeMerger,
                datesBySample.get(bugsData.getSampleCODE()));
        if(rangeMerger.shouldMerge(mapping)){
            rangeMerger.doMerge(mapping);
        } else {
            super.add(mapping);
            addToCache(mapping);
        }
    }

    private void addToCache(BugsListSeadMapping<DatesCalendar, RelativeDate> mapping){
        if(mapping.isErrorFree() && mapping.getSeadData() != null && !mapping.getSeadData().isEmpty()){
            String sampleCODE = mapping.getBugsData().getSampleCODE();
            List<BugsListSeadMapping<DatesCalendar, RelativeDate>> storedDates = datesBySample.get(sampleCODE);
            if(storedDates == null){
                storedDates = new ArrayList<>();
                datesBySample.put(sampleCODE, storedDates);
            }
            storedDates.add(mapping);
        }
    }
}
