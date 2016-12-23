package se.sead.bugs;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import se.sead.bugsimport.site.bugsmodel.BugsSite;
import se.sead.bugsimport.site.bugsmodel.BugsSiteBugsTable;
import se.sead.testutils.DefaultConfig;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AccessSearchTest {

    @TestConfiguration
    static class Config extends DefaultConfig {
        Config(){
            super("bugs", "accesssearch.mdb", "");
        }

        @Override
        protected String getDataFile() {
            return "";
        }
    }

    @Autowired
    private AccessDatabaseProvider databaseProvider;
    private BugsTable<BugsSite> siteTable = new BugsSiteBugsTable();
    private AccessSearcher<BugsSite> siteSearcher;

    private BugsSite expectedSite01;
    private BugsSite expectedSite02;
    private BugsSite expectedSite03;

    @Before
    public void setup(){
        siteSearcher = new AccessSearcher<>(databaseProvider.getReader());
        expectedSite01 = createBugsSite("SITE000001", "Site name", null, null);
        expectedSite02 = createBugsSite("SITE000002", "Abydos mummies", 26.145428f, 86.0f);
        expectedSite03 = createBugsSite("SITE000003", null, null, null);
    }

    private BugsSite createBugsSite(String code, String name, Float latitude, Float altitude){
        BugsSite bugsSite = new BugsSite();
        bugsSite.setCode(code);
        bugsSite.setName(name);
        bugsSite.setLatDD(latitude);
        bugsSite.setAlt(altitude);
        return bugsSite;
    }

    @Test
    public void searchForStringValue(){
        AccessSearcher.SearchCriteria<String> criteria = new AccessSearcher.SearchCriteria<>("SiteCODE", "SITE000001");
        Optional<BugsSite> result = siteSearcher.search(siteTable, criteria);
        assertTrue(result.isPresent());
        assertEquals(expectedSite01, result.get());
    }

    @Test
    public void searchForFloatValue(){
        AccessSearcher.SearchCriteria<Float> criteria = new AccessSearcher.SearchCriteria<>("LatDD", 26.145428f);
        Optional<BugsSite> result = siteSearcher.search(siteTable, criteria);
        assertTrue(result.isPresent());
        assertEquals(expectedSite02, result.get());
    }

    @Test
    public void searchForNullValue(){
        AccessSearcher.SearchCriteria<String> criteria = new AccessSearcher.SearchCriteria<>("SiteName", null);
        Optional<BugsSite> result = siteSearcher.search(siteTable, criteria);
        assertTrue(result.isPresent());
        assertEquals(expectedSite03, result.get());
    }

    @Test(expected = AccessSearcher.MultipleResultException.class)
    public void multipleFindsGetFirst(){
        AccessSearcher.SearchCriteria<String> criteria = new AccessSearcher.SearchCriteria<>("Region", null);
        Optional<BugsSite> result = siteSearcher.search(siteTable, criteria);
    }

    @Test
    public void noResults(){
        AccessSearcher.SearchCriteria<String> criteria = new AccessSearcher.SearchCriteria<>("SiteName", "Wrong name");
        Optional<BugsSite> result = siteSearcher.search(siteTable, criteria);
        assertFalse(result.isPresent());
    }
}
