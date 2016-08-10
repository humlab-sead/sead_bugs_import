package se.sead.siteotherproxies;

import org.junit.Test;
import se.sead.AccessReaderTest;
import se.sead.bugsimport.siteotherproxies.bugsmodel.SiteOtherProxies;
import se.sead.bugsimport.siteotherproxies.bugsmodel.SiteOtherProxiesBugsTable;

import java.util.Comparator;

public class SiteOtherProxiesReadTest extends AccessReaderTest<SiteOtherProxies> {

    public SiteOtherProxiesReadTest() {
        super("siteotherproxies");
    }

    @Test
    public void run() {
        readTableFromDefaultFolder(
                "siteotherproxies.mdb",
                new SiteOtherProxiesBugsTable(),
                SiteOtherProxiesExpectedBugsTableData.EXPECTED_DATA,
                new TestSiteOtherProxiesComparator());
    }

    private static class TestSiteOtherProxiesComparator implements Comparator<SiteOtherProxies> {
        @Override
        public int compare(SiteOtherProxies o1, SiteOtherProxies o2) {
            return o1.getSiteCode().compareTo(o2.getSiteCode());
        }
    }
}
