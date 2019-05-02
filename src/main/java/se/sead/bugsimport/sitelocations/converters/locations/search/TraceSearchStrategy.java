package se.sead.bugsimport.sitelocations.converters.locations.search;

import se.sead.bugsimport.site.seadmodel.SeadSite;
import se.sead.bugsimport.sitelocations.bugsmodel.BugsSiteLocation;
import se.sead.bugsimport.sitelocations.converters.SiteLocationTraceHelper;
import se.sead.bugsimport.sitelocations.seadmodel.SiteLocation;

public class TraceSearchStrategy implements SearchStrategies {
    private SiteLocationTraceHelper traceHelper;
    public TraceSearchStrategy(SiteLocationTraceHelper traceHelper){
        this.traceHelper = traceHelper;
    }
    @Override
    public SiteLocation getSiteLocation(SeadSite site, BugsSiteLocation bugsSiteLocation) {
        return traceHelper.getFromLastTrace(bugsSiteLocation.compressToString());
    }
}
