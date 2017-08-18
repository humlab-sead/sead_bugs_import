package se.sead.bugsimport.periods;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.BugsTableRowConverter;
import se.sead.bugsimport.periods.bugsmodel.Period;
import se.sead.bugsimport.periods.converters.PeriodTraceHelper;
import se.sead.bugsimport.periods.converters.RelativeAgeUpdater;
import se.sead.bugsimport.periods.seadmodel.RelativeAge;
import se.sead.utils.errorlog.IgnoredItemErrorLog;

@Component
public class PeriodRowConverter implements BugsTableRowConverter<Period, RelativeAge> {

    @Autowired
    private PeriodTraceHelper traceHelper;
    @Autowired
    private RelativeAgeUpdater updater;

    @Override
    public RelativeAge convertForDataRow(Period bugsData) {
        if (shouldIgnore(bugsData)) {
            return new IgnoreRelativeAge();
        }
        RelativeAge fromLastTrace = traceHelper.getFromLastTrace(bugsData.getBugsIdentifier());
        if(fromLastTrace == null){
            return create(bugsData);
        } else if(fromLastTrace.isErrorFree()){
            return update(fromLastTrace, bugsData);
        } else {
            return fromLastTrace;
        }
    }

    private boolean shouldIgnore(Period bugsData){
        return bugsData.getPeriodCode().equals("?");
    }

    private RelativeAge create(Period bugsData){
        return update(new RelativeAge(), bugsData);
    }

    private RelativeAge update(RelativeAge relativeAge, Period bugsData){
        updater.update(relativeAge, bugsData);
        return relativeAge;
    }

    private static class IgnoreRelativeAge extends RelativeAge {
        IgnoreRelativeAge(){
            addError(new IgnoredItemErrorLog("This item is ignored"));
        }
    }
}
