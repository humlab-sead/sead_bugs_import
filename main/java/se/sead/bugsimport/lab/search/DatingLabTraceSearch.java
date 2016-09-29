package se.sead.bugsimport.lab.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.lab.bugsmodel.BugsLab;
import se.sead.bugsimport.lab.bugsmodel.LabBugsTable;
import se.sead.bugsimport.lab.seadmodel.DatingLab;
import se.sead.bugsimport.tracing.SeadDataFromTraceHelper;
import se.sead.repositories.DatingLabRepository;

@Component
@Order(1)
public class DatingLabTraceSearch implements DatingLabSearch {

    @Autowired
    private DatingLabTraceHelper traceHelper;

    @Override
    public DatingLab findFor(BugsLab bugsLab) {
        DatingLab fromLastTrace = traceHelper.getFromLastTrace(bugsLab.getBugsIdentifier());
        if(fromLastTrace == null){
            return NO_LAB_FOUND;
        } else {
            return fromLastTrace;
        }
    }

    @Component
    public static class DatingLabTraceHelper extends SeadDataFromTraceHelper<BugsLab, DatingLab> {

        @Autowired
        public DatingLabTraceHelper(DatingLabRepository repository){
            super(LabBugsTable.TABLE_NAME, false, repository);
        }
    }
}
