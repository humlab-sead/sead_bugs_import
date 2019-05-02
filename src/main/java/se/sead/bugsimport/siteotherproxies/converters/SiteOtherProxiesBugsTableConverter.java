package se.sead.bugsimport.siteotherproxies.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.BugsTableRowConverter;
import se.sead.bugsimport.site.bugsmodel.BugsSite;
import se.sead.bugsimport.site.seadmodel.SeadSite;
import se.sead.bugsimport.siteotherproxies.bugsmodel.SiteOtherProxies;
import se.sead.bugsimport.siteotherproxies.seadmodel.SiteOtherRecord;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.bugsimport.tracing.seadmodel.BugsTraceType;
import se.sead.repositories.BugsTraceRepository;
import se.sead.repositories.SiteOtherRecordRepository;
import se.sead.repositories.SiteRepository;
import se.sead.sead.recordtypes.RecordTypeCache;

import java.util.Arrays;
import java.util.List;

@Component
public class SiteOtherProxiesBugsTableConverter implements BugsTableRowConverter<SiteOtherProxies, SiteOtherRecord> {

    @Autowired
    private BugsTraceRepository traceRepository;
    @Autowired
    private SiteRepository siteRepository;
    @Autowired
    private SiteOtherRecordRepository siteOtherRecordRepository;
    @Autowired
    private RecordTypeCache recordTypeCache;

    private String bugsSiteTableName;

    public SiteOtherProxiesBugsTableConverter(){
        bugsSiteTableName = new BugsSite().bugsTable();
    }

    @Override
    public List<SiteOtherRecord> convertListForDataRow(SiteOtherProxies bugsData) {
        SeadSite seadSiteFromBugsCode = getSeadSiteFromBugsCode(bugsData.getSiteCode());
        if(seadSiteFromBugsCode == null){
            return createFollowUpError();
        }
        List<SiteOtherRecord> storedVersions = siteOtherRecordRepository.findAllBySite(seadSiteFromBugsCode);
        if(storedVersions.isEmpty()){
            return createNewItems(bugsData, seadSiteFromBugsCode);
        } else {
            return updateVersions(bugsData, seadSiteFromBugsCode, storedVersions);
        }
    }

    private SeadSite getSeadSiteFromBugsCode(String bugsSiteCode){
        BugsTrace siteTrace = getLatestEvent(bugsSiteCode);
        return siteRepository.findOne(siteTrace.getSeadId());
    }

    private BugsTrace getLatestEvent(String bugsSiteCode) {
        List<BugsTrace> siteTraces = traceRepository.findByBugsTableAndBugsIdentifierOrderByChangeDate(bugsSiteTableName, bugsSiteCode);
        if(siteTraces.isEmpty() || siteTraces.get(0).getType() == BugsTraceType.DELETE){
            return BugsTrace.NO_TRACE;
        } else {
            return siteTraces.get(0);
        }
    }

    private List<SiteOtherRecord> createFollowUpError(){
        SiteOtherRecord errorHolder = new SiteOtherRecord();
        errorHolder.addError("Site is not imported, ignoring");
        return Arrays.asList(errorHolder);
    }

    private List<SiteOtherRecord> createNewItems(SiteOtherProxies bugsData, SeadSite site){
        return new SiteOtherRecordCreator(bugsData, recordTypeCache, site).create();
    }

    private List<SiteOtherRecord> updateVersions(SiteOtherProxies bugsData, SeadSite site, List<SiteOtherRecord> priorVersions){
        new SiteOtherRecordUpdater(bugsData, recordTypeCache, site).update(priorVersions);
        return priorVersions;
    }

}
