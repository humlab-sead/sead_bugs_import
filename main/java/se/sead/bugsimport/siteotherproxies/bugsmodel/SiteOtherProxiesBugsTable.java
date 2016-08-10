package se.sead.bugsimport.siteotherproxies.bugsmodel;

import com.healthmarketscience.jackcess.Row;
import se.sead.bugs.BugsTable;
import se.sead.bugsimport.siteotherproxies.converters.BugsProxyToSeadRecordTypeNames;

public class SiteOtherProxiesBugsTable extends BugsTable<SiteOtherProxies> {

    static final String TABLE_NAME = "TSiteOtherProxies";

    public SiteOtherProxiesBugsTable(){
        super(TABLE_NAME);
    }

    @Override
    public SiteOtherProxies createItem(Row accessRow) {
        SiteOtherProxies otherProxy = new SiteOtherProxies();
        otherProxy.setSiteCode(accessRow.getString("SiteCODE"));
        if(accessRow.getBoolean("HasPollen")){
            otherProxy.addProxy(BugsProxyToSeadRecordTypeNames.Pollen);
        }
        if(accessRow.getBoolean("HasPlantMacro")){
            otherProxy.addProxy(BugsProxyToSeadRecordTypeNames.PlantMacro);
        }
        if(accessRow.getBoolean("HasDiatoms")){
            otherProxy.addProxy(BugsProxyToSeadRecordTypeNames.Diatoms);
        }
        if(accessRow.getBoolean("HasChironomids")){
            otherProxy.addProxy(BugsProxyToSeadRecordTypeNames.Chironomids);
        }
        if(accessRow.getBoolean("HasSoilChemistry")){
            otherProxy.addProxy(BugsProxyToSeadRecordTypeNames.SoilChemistry);
        }
        if(accessRow.getBoolean("HasIsotopes")){
            otherProxy.addProxy(BugsProxyToSeadRecordTypeNames.Isotopes);
        }
        if(accessRow.getBoolean("HasAnimalBones")){
            otherProxy.addProxy(BugsProxyToSeadRecordTypeNames.AnimalBones);
        }
        if(accessRow.getBoolean("HasArchaeology")){
            otherProxy.addProxy(BugsProxyToSeadRecordTypeNames.Archaeology);
        }
        if(accessRow.getBoolean("HasMolluscs")){
            otherProxy.addProxy(BugsProxyToSeadRecordTypeNames.Molluscs);
        }
        return otherProxy;
    }
}
