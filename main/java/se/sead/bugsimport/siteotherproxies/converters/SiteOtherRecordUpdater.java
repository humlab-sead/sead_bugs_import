package se.sead.bugsimport.siteotherproxies.converters;

import se.sead.bugsimport.site.seadmodel.SeadSite;
import se.sead.bugsimport.siteotherproxies.bugsmodel.SiteOtherProxies;
import se.sead.bugsimport.siteotherproxies.seadmodel.SiteOtherRecord;
import se.sead.sead.recordtypes.RecordTypeCache;

import java.util.List;

public class SiteOtherRecordUpdater extends SiteOtherRecordCreator {

    public SiteOtherRecordUpdater(SiteOtherProxies bugsData, RecordTypeCache recordTypeCache, SeadSite site) {
        super(bugsData, recordTypeCache, site);
    }

    void update(List<SiteOtherRecord> priorVersions){
        List<SiteOtherRecord> importVersions = super.create();
        handleNewItems(importVersions, priorVersions);
        handleDeletedItems(importVersions, priorVersions);
    }

    private void handleNewItems(List<SiteOtherRecord> importVersions, List<SiteOtherRecord> priorVersions) {
        for (SiteOtherRecord importVersion :
                importVersions) {
            if (!priorVersions.contains(importVersion)){
                priorVersions.add(importVersion);
            }
        }
    }

    private void handleDeletedItems(List<SiteOtherRecord> importVersions, List<SiteOtherRecord> priorVersions){
        for (SiteOtherRecord priorItem :
                priorVersions) {
            if(!importVersions.contains(priorItem)) {
                priorItem.markForDeletion();
            }
        }
    }
}
