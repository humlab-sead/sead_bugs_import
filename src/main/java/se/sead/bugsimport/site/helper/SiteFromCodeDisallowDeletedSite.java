package se.sead.bugsimport.site.helper;

import org.springframework.stereotype.Component;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.bugsimport.tracing.seadmodel.BugsTraceType;

import java.util.List;

@Component
public class SiteFromCodeDisallowDeletedSite extends SiteFromTrace {

    @Override
    protected BugsTrace filteredResults(List<BugsTrace> siteTraces) {
        if(siteTraces.isEmpty() || siteTraces.get(0).getType() == BugsTraceType.DELETE){
            return BugsTrace.NO_TRACE;
        } else {
            return siteTraces.get(0);
        }
    }
}
