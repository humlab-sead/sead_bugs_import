package se.sead.bugsimport.datescalendar.cache;

import se.sead.bugsimport.MappingResult;
import se.sead.bugsimport.datescalendar.bugsmodel.DatesCalendar;
import se.sead.bugsimport.datesperiod.seadmodel.RelativeDate;

class UncertaintyDatesCalendarContainer {
    private MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate> from;
    private MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate> to;

    UncertaintyDatesCalendarContainer(MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate> from, MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate> to){
        this.from = from;
        this.to = to;
    }

    boolean rangeShouldBeIncluded(){
        return from != UncertaintyExtractor.NO_DATES_CALENDAR_FOR_UNCERTAINTY ||
                to != UncertaintyExtractor.NO_DATES_CALENDAR_FOR_UNCERTAINTY;
    }

    public DatesCalendar getFromDate() {
        return from.getBugsData();
    }

    public DatesCalendar getToDate() {
        return to.getBugsData();
    }

    public MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate> getFromMapping(){
        return from;
    }

    public MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate> getToMapping(){
        return to;
    }

    public boolean canTryToMergeDifferences(){
        return from != UncertaintyExtractor.NO_DATES_CALENDAR_FOR_UNCERTAINTY &&
                to != UncertaintyExtractor.NO_DATES_CALENDAR_FOR_UNCERTAINTY;
    }
}
