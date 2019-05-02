package se.sead.bugsimport.datesperiod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.BugsSeadMapper;
import se.sead.bugsimport.datesperiod.bugsmodel.DatesPeriod;
import se.sead.bugsimport.datesperiod.bugsmodel.DatesPeriodBugsTable;
import se.sead.bugsimport.datesperiod.seadmodel.RelativeDate;

@Component
public class DatesPeriodBugsSeadMapper extends BugsSeadMapper<DatesPeriod, RelativeDate> {

    @Autowired
    public DatesPeriodBugsSeadMapper(DatesPeriodRowConverter singleBugsTableRowConverterForMapper) {
        super(new DatesPeriodBugsTable(), singleBugsTableRowConverterForMapper);
    }
}
