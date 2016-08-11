package se.sead.sitereferences;

import org.junit.Test;
import se.sead.AccessReaderTest;
import se.sead.bugsimport.sitereferences.bugsmodel.BugsSiteRef;
import se.sead.bugsimport.sitereferences.bugsmodel.SiteRefBugsTable;

import java.util.Comparator;

public class SiteReferencesReadTest extends AccessReaderTest<BugsSiteRef> {

    public SiteReferencesReadTest() {
        super("sitereferences");
    }

    @Test
    public void run(){
        super.readTableFromDefaultFolder(
                "sitereferences.mdb",
                new SiteRefBugsTable(),
                ExpectedBugsSiteRefData.EXPECTED_BUGS_DATA,
                new BugsSiteRefComparator()
        );
    }

    private static class BugsSiteRefComparator implements Comparator<BugsSiteRef> {

        @Override
        public int compare(BugsSiteRef o1, BugsSiteRef o2) {
            return o1.compressToString().compareTo(o2.compressToString());
        }
    }
}
