package se.sead.bugsimport.datescalendar.cache;

import se.sead.bugsimport.BugsListSeadMapping;
import se.sead.bugsimport.datescalendar.bugsmodel.DatesCalendar;
import se.sead.bugsimport.datesperiod.seadmodel.RelativeDate;

class UncertaintyDatesCalendarContainer {
    private BugsListSeadMapping<DatesCalendar, RelativeDate> from;
    private BugsListSeadMapping<DatesCalendar, RelativeDate> to;

    UncertaintyDatesCalendarContainer(BugsListSeadMapping<DatesCalendar, RelativeDate> from, BugsListSeadMapping<DatesCalendar, RelativeDate> to){
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

    public BugsListSeadMapping<DatesCalendar, RelativeDate> getFromMapping(){
        return from;
    }

    public BugsListSeadMapping<DatesCalendar, RelativeDate> getToMapping(){
        return to;
    }

    public boolean canTryToMergeDifferences(){
        return from != UncertaintyExtractor.NO_DATES_CALENDAR_FOR_UNCERTAINTY &&
                to != UncertaintyExtractor.NO_DATES_CALENDAR_FOR_UNCERTAINTY;
    }
}
