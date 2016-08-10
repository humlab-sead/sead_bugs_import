package se.sead.bugsimport.siteotherproxies.converters;

import se.sead.bugsimport.site.seadmodel.SeadSite;
import se.sead.bugsimport.siteotherproxies.bugsmodel.SiteOtherProxies;
import se.sead.bugsimport.siteotherproxies.seadmodel.SiteOtherRecord;
import se.sead.sead.recordtypes.RecordTypeCache;

import java.util.ArrayList;
import java.util.List;

class SiteOtherRecordCreator {

    private RecordTypeCache recordTypeCache;

    private SiteOtherProxies bugsData;
    private SeadSite owner;
    private List<SiteOtherRecord> seadVersion;

    SiteOtherRecordCreator(SiteOtherProxies bugsData, RecordTypeCache recordTypeCache, SeadSite site){
        this.bugsData = bugsData;
        this.recordTypeCache = recordTypeCache;
        owner = site;
        seadVersion = new ArrayList<>();
    }

    List<SiteOtherRecord> create(){
        seadVersion = new ArrayList<>();
        for (BugsProxyToSeadRecordTypeNames proxyName :
                bugsData.getRegisteredProxies()) {
            seadVersion.add(create(proxyName));
        }
        return seadVersion;
    }

    private SiteOtherRecord create(BugsProxyToSeadRecordTypeNames type){
        SiteOtherRecord record = new SiteOtherRecord();
        record.setSite(owner);
        record.setRecordType(recordTypeCache.getByName(type.getSeadName()));
        return record;
    }
}
