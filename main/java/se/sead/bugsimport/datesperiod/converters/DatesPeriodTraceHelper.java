package se.sead.bugsimport.datesperiod.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.datesperiod.bugsmodel.DatesPeriod;
import se.sead.bugsimport.datesperiod.bugsmodel.DatesPeriodBugsTable;
import se.sead.bugsimport.datesperiod.seadmodel.RelativeDate;
import se.sead.bugsimport.tracing.SeadDataFromTraceHelper;
import se.sead.repositories.RelativeDateRepository;

@Component
public class DatesPeriodTraceHelper extends SeadDataFromTraceHelper<DatesPeriod, RelativeDate> {

    @Autowired
    public DatesPeriodTraceHelper(RelativeDateRepository repository){
        super(DatesPeriodBugsTable.TABLE_NAME, "tbl_relative_dates", false, repository);
    }
}
