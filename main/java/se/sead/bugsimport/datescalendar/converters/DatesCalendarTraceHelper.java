package se.sead.bugsimport.datescalendar.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.datescalendar.bugsmodel.DatesCalendar;
import se.sead.bugsimport.datescalendar.bugsmodel.DatesCalendarBugsTable;
import se.sead.bugsimport.datesperiod.seadmodel.RelativeDate;
import se.sead.bugsimport.tracing.SeadDataFromTraceHelper;
import se.sead.repositories.RelativeDateRepository;

@Component
public class DatesCalendarTraceHelper extends SeadDataFromTraceHelper<DatesCalendar, RelativeDate> {

    @Autowired
    public DatesCalendarTraceHelper(RelativeDateRepository repository){
        super(DatesCalendarBugsTable.TABLE_NAME, "tbl_relative_dates", false, repository);
    }
}
