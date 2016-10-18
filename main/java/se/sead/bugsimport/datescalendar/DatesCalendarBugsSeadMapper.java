package se.sead.bugsimport.datescalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.BugsSeadMapper;
import se.sead.bugsimport.datescalendar.bugsmodel.DatesCalendar;
import se.sead.bugsimport.datescalendar.bugsmodel.DatesCalendarBugsTable;
import se.sead.bugsimport.datesperiod.seadmodel.RelativeDate;

@Component
public class DatesCalendarBugsSeadMapper extends BugsSeadMapper<DatesCalendar, RelativeDate> {

    @Autowired
    public DatesCalendarBugsSeadMapper(DatesCalendarRowConverter singleBugsTableRowConverterForMapper) {
        super(new DatesCalendarBugsTable(), singleBugsTableRowConverterForMapper);
    }
}
