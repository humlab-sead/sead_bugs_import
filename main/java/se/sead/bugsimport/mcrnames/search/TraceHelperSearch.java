package se.sead.bugsimport.mcrnames.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.mcrnames.bugsmodel.BugsMCRNames;
import se.sead.bugsimport.mcrnames.converters.McrNameTracerHelper;
import se.sead.bugsimport.mcrnames.seadmodel.MCRName;

@Component
@Order(1)
public class TraceHelperSearch implements MCRSearch{

    @Autowired
    private McrNameTracerHelper tracerHelper;

    @Override
    public MCRName findFor(BugsMCRNames bugsData) {
        MCRName fromLastTrace = tracerHelper.getFromLastTrace(bugsData.getBugsIdentifier());
        if(fromLastTrace == null){
            return NO_MCR_NAME_FOUND;
        }
        return fromLastTrace;
    }
}
