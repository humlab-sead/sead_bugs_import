package se.sead.bugsimport.site.helper;

import org.springframework.beans.factory.annotation.Autowired;
import se.sead.bugsimport.site.bugsmodel.BugsSite;
import se.sead.bugsimport.site.seadmodel.SeadSite;
import se.sead.bugsimport.tracing.SeadDataFromTraceHelper;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.repositories.BugsTraceRepository;
import se.sead.repositories.SiteRepository;

import java.util.List;

abstract class SiteFromTrace {
    private static final String BUGS_SITE_TABLE_NAME = new BugsSite().bugsTable();

    @Autowired
    private BugsTraceRepository traceRepository;

    @Autowired
    private SiteRepository siteRepository;

    public SeadSite getSeadSiteFromBugsCode(String bugsSiteCode){
        BugsTrace latest = getLatest(bugsSiteCode);
        SeadSite site = getSeadSiteFromTrace(latest);
        if(siteExistsAndHasBeenEditedInSeadSinceImport(site, latest)){
            return createErrorSite("Sead data has been updated since last bugs import");
        }
        return site;
    }

    public BugsTrace getLatest(String bugsSiteCode){
        List<BugsTrace> siteTraces = traceRepository.findByBugsTableAndBugsIdentifierOrderByChangeDate(BUGS_SITE_TABLE_NAME, bugsSiteCode);
        return filteredResults(siteTraces);
    }

    public SeadSite getSeadSiteFromTrace(BugsTrace trace){
        return siteRepository.findOne(trace.getSeadId());
    }

    protected abstract BugsTrace filteredResults(List<BugsTrace> siteTraces);

    private boolean siteExistsAndHasBeenEditedInSeadSinceImport(SeadSite seadSite, BugsTrace latestTrace){
        return SeadDataFromTraceHelper.seadDataExistsAndHasBeenEditedSinceImport(seadSite, latestTrace);
    }

    protected SeadSite createErrorSite(String errorMessage){
        SeadSite errorSite = new SeadSite();
        errorSite.addError(errorMessage);
        return errorSite;
    }

}
