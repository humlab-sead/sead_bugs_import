package se.sead.bugsimport.rdb.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.rdb.bugsmodel.BugsRDB;
import se.sead.bugsimport.rdb.seadmodel.Rdb;

@Component
@Order(1)
public class LatestTraceSearch implements RdbSearch {

    @Autowired
    private RdbTraceHelper traceHelper;

    @Override
    public Rdb findFor(BugsRDB rdb) {
        Rdb fromLastTrace = traceHelper.getFromLastTrace(rdb.compressToString());
        if(fromLastTrace == null){
            return NO_RDB_FOUND;
        }
        return fromLastTrace;
    }
}
