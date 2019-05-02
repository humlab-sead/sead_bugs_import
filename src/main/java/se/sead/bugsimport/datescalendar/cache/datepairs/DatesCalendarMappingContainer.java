package se.sead.bugsimport.datescalendar.cache.datepairs;

import se.sead.bugsimport.BugsListSeadMapping;
import se.sead.bugsimport.datescalendar.bugsmodel.DatesCalendar;
import se.sead.bugsimport.datescalendar.cache.datepairs.UncertaintyExtractor;
import se.sead.bugsimport.datesperiod.seadmodel.RelativeDate;

import java.util.Collections;

public class DatesCalendarMappingContainer {

    public static final BugsListSeadMapping<DatesCalendar, RelativeDate> NO_DATES_CALENDAR_FOR_UNCERTAINTY = new BugsListSeadMapping<>(new DatesCalendar(), Collections.EMPTY_LIST);

    private BugsListSeadMapping<DatesCalendar, RelativeDate> from;
    private BugsListSeadMapping<DatesCalendar, RelativeDate> to;

    public DatesCalendarMappingContainer(BugsListSeadMapping<DatesCalendar, RelativeDate> from, BugsListSeadMapping<DatesCalendar, RelativeDate> to){
        this.from = from;
        this.to = to;
    }

    public boolean rangeShouldBeIncluded(){
        return from != NO_DATES_CALENDAR_FOR_UNCERTAINTY ||
                to != NO_DATES_CALENDAR_FOR_UNCERTAINTY;
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

    public boolean isClosedRange(){
        return fromDateIsSet() && toDateIsSet();
    }

    public BugsListSeadMapping<DatesCalendar, RelativeDate> getToMapping(){
        return to;
    }

    public boolean fromDateIsSet(){
        return from != NO_DATES_CALENDAR_FOR_UNCERTAINTY;
    }

    public boolean toDateIsSet(){
        return to != NO_DATES_CALENDAR_FOR_UNCERTAINTY;
    }

    public boolean canTryToMergeDifferences(){
        return from != NO_DATES_CALENDAR_FOR_UNCERTAINTY &&
                to != NO_DATES_CALENDAR_FOR_UNCERTAINTY;
    }
}
