package se.sead.bugsimport.siteotherproxies.bugsmodel;

import se.sead.bugs.TraceableBugsData;
import se.sead.bugsimport.siteotherproxies.converters.BugsProxyToSeadRecordTypeNames;

import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

public class SiteOtherProxies extends TraceableBugsData{

    private String siteCode;

    private Set<BugsProxyToSeadRecordTypeNames> registeredProxies = EnumSet.noneOf(BugsProxyToSeadRecordTypeNames.class);

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public void addProxy(BugsProxyToSeadRecordTypeNames proxy){
        registeredProxies.add(proxy);
    }

    public Set<BugsProxyToSeadRecordTypeNames> getRegisteredProxies(){ return registeredProxies;}

    @Override
    public String compressToString() {
        return "{" + siteCode + ":" +
                String.join("|",
                        registeredProxies
                                .stream()
                                .map(proxy -> proxy.name())
                                .collect(Collectors.toList())
                ) + "}";
    }

    @Override
    public String bugsTable() {
        return SiteOtherProxiesBugsTable.TABLE_NAME;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SiteOtherProxies that = (SiteOtherProxies) o;

        if (!siteCode.equals(that.siteCode)) return false;
        if(!registeredProxies.equals(that.registeredProxies)){
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = siteCode.hashCode();
        result = 31 * result + registeredProxies.hashCode();
        return result;
    }

    @Override
    public String toString(){
        return compressToString();
    }
}
