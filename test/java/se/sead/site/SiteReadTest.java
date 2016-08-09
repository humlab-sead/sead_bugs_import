package se.sead.site;

import org.junit.Test;
import se.sead.AccessReaderTest;
import se.sead.bugsimport.site.bugsmodel.BugsSite;
import se.sead.bugsimport.site.bugsmodel.BugsSiteBugsTable;

import java.util.Comparator;

public class SiteReadTest extends AccessReaderTest<BugsSite> {

    public SiteReadTest(){
        super("site");
    }

    @Test
    public void read(){
        readTableFromDefaultFolder("site.mdb", new BugsSiteBugsTable(), SiteTestDefinition.EXPECTED_BUGS_DATA, new TestSiteComparator());
    }

    private static class TestSiteComparator implements Comparator<BugsSite>{
        @Override
        public int compare(BugsSite o1, BugsSite o2) {
            return o1.getCode().compareTo(o2.getCode());
        }
    }
}
