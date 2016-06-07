package se.sead.site;

import org.junit.Test;
import se.sead.AccessReaderTest;
import se.sead.bugsimport.site.bugsmodel.Site;
import se.sead.bugsimport.site.bugsmodel.SiteBugsTable;

public class SiteReadTest extends AccessReaderTest<Site> {

    public SiteReadTest(){
        super("site");
    }

    @Test
    public void read(){
        readTableFromDefaultFolder("site.mdb", new SiteBugsTable(), Expected);
    }
}
