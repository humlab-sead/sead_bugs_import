package se.sead.bugsimport.datescalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.BugsTableRowConverter;
import se.sead.bugsimport.datescalendar.bugsmodel.DatesCalendar;
import se.sead.bugsimport.datescalendar.converters.DatesCalendarTraceHelper;
import se.sead.bugsimport.datescalendar.converters.RelativeDateUpdaterForCalendar;
import se.sead.bugsimport.datesperiod.seadmodel.RelativeDate;

@Component
public class DatesCalendarRowConverter implements BugsTableRowConverter<DatesCalendar, RelativeDate> {

    @Autowired
    private DatesCalendarTraceHelper traceHelper;
    @Autowired
    private RelativeDateUpdaterForCalendar updater;

    @Override
    public RelativeDate convertForDataRow(DatesCalendar bugsData) {
        RelativeDate fromLastTrace = traceHelper.getFromLastTrace(bugsData.getBugsIdentifier());
        if(fromLastTrace == null){
            return create(bugsData);
        } else if(fromLastTrace.isErrorFree()){
            return update(fromLastTrace, bugsData);
        } else {
            return fromLastTrace;
        }
    }

    private RelativeDate create(DatesCalendar bugsData){
        return update(new RelativeDate(), bugsData);
    }

    private RelativeDate update(RelativeDate original, DatesCalendar bugsData){
        updater.update(original, bugsData);
        return original;
    }
}
