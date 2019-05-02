package se.sead.siteotherproxies;

import se.sead.bugsimport.siteotherproxies.bugsmodel.SiteOtherProxies;
import se.sead.bugsimport.siteotherproxies.converters.BugsProxyToSeadRecordTypeNames;

import java.util.Arrays;
import java.util.List;

import static se.sead.bugsimport.siteotherproxies.converters.BugsProxyToSeadRecordTypeNames.*;

public class SiteOtherProxiesExpectedBugsTableData {

    static List<SiteOtherProxies> EXPECTED_DATA =
            Arrays.asList(
                    create("SITE000006", PlantMacro, Archaeology),
                    create("SITE000008", Archaeology),
                    create("SITE000009", Pollen, PlantMacro, AnimalBones),
                    create("SITE000010", Pollen, PlantMacro, AnimalBones, Archaeology, Molluscs)
            );

    static SiteOtherProxies create(
            String siteCode,
            BugsProxyToSeadRecordTypeNames... proxyNames
    ){
        SiteOtherProxies otherProxy = new SiteOtherProxies();
        otherProxy.setSiteCode(siteCode);
        if(proxyNames != null){
            for (BugsProxyToSeadRecordTypeNames proxyName :
                    proxyNames) {
                otherProxy.addProxy(proxyName);
            }
        }
        return otherProxy;
    }
}
