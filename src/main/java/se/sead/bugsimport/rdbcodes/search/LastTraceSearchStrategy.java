package se.sead.bugsimport.rdbcodes.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.rdbcodes.bugsmodel.BugsRDBCodes;
import se.sead.bugsimport.rdbcodes.converter.BugsRdbCodeTraceHelper;
import se.sead.bugsimport.rdbcodes.seadmodel.RdbCode;

@Component
public class LastTraceSearchStrategy implements SearchStrategy{

    @Autowired
    private BugsRdbCodeTraceHelper traceHelper;

    @Override
    public RdbCode get(BugsRDBCodes bugsData) {
        RdbCode fromLastTrace = traceHelper.getFromLastTrace(bugsData.getBugsIdentifier());
        if(fromLastTrace == null){
            return NO_CODE_FOUND;
        }
        return fromLastTrace;
    }
}
