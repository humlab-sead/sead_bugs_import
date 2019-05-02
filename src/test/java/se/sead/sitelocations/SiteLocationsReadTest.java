package se.sead.sitelocations;

import org.junit.Test;
import se.sead.AccessReaderTest;
import se.sead.bugsimport.sitelocations.bugsmodel.BugsSiteLocation;
import se.sead.bugsimport.sitelocations.bugsmodel.BugsSiteLocationBugsTable;

public class SiteLocationsReadTest extends AccessReaderTest<BugsSiteLocation> {

    public SiteLocationsReadTest(){
        super("sitelocations");
    }

    @Test
    public void run(){
        readTableFromDefaultFolder(
                "sitelocations.mdb",
                new BugsSiteLocationBugsTable(),
                ExpectedBugsData.EXPECTED_BUGS_DATA,
                (site1, site2) -> site1.compressToString().compareTo(site2.compressToString()));
    }
}
