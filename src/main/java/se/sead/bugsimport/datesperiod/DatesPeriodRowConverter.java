package se.sead.bugsimport.datesperiod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.BugsTableRowConverter;
import se.sead.bugsimport.datesperiod.bugsmodel.DatesPeriod;
import se.sead.bugsimport.datesperiod.converters.DatesPeriodTraceHelper;
import se.sead.bugsimport.datesperiod.converters.RelativeDatesUpdaterForPeriod;
import se.sead.bugsimport.datesperiod.seadmodel.RelativeDate;

@Component
public class DatesPeriodRowConverter implements BugsTableRowConverter<DatesPeriod, RelativeDate> {

    @Autowired
    private DatesPeriodTraceHelper traceHelper;
    @Autowired
    private RelativeDatesUpdaterForPeriod updater;

    @Override
    public RelativeDate convertForDataRow(DatesPeriod bugsData) {
        RelativeDate fromLastTrace = traceHelper.getFromLastTrace(bugsData.getBugsIdentifier());
        if(fromLastTrace == null){
            return create(bugsData);
        } else if(fromLastTrace.isErrorFree()){
            return update(fromLastTrace, bugsData);
        } else {
            return fromLastTrace;
        }
    }

    private RelativeDate create(DatesPeriod bugsData){
        return update(new RelativeDate(), bugsData);
    }

    private RelativeDate update(RelativeDate original, DatesPeriod bugsData){
        updater.update(original, bugsData);
        return original;
    }
}
