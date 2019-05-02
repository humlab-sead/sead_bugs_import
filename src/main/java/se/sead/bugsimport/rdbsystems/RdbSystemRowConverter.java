package se.sead.bugsimport.rdbsystems;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.BugsTableRowConverter;
import se.sead.bugsimport.rdbsystems.bugsmodel.BugsRDBSystem;
import se.sead.bugsimport.rdbsystems.converters.RdbSystemFromTrace;
import se.sead.bugsimport.rdbsystems.converters.RdbSystemUpdater;
import se.sead.bugsimport.rdbsystems.seadmodel.RdbSystem;

@Component
public class RdbSystemRowConverter implements BugsTableRowConverter<BugsRDBSystem, RdbSystem> {

    @Autowired
    private RdbSystemUpdater updater;

    @Autowired
    private RdbSystemFromTrace traceHelper;

    @Override
    public RdbSystem convertForDataRow(BugsRDBSystem bugsData) {
        RdbSystem fromTrace = getFromTrace(bugsData);
        if(fromTrace != null && !fromTrace.isErrorFree()){
            return fromTrace;
        }
        if(fromTrace != null){
            return update(fromTrace, bugsData);
        } else {
            return create(bugsData);
        }
    }

    private RdbSystem getFromTrace(BugsRDBSystem bugsData) {
        return traceHelper.getFromLastTrace(bugsData.getBugsIdentifier());
    }

    private RdbSystem update(RdbSystem fromTrace, BugsRDBSystem bugsData) {
        return updater.update(fromTrace, bugsData);
    }

    private RdbSystem create(BugsRDBSystem bugsData) {
        return updater.update(new RdbSystem(), bugsData);
    }


}
