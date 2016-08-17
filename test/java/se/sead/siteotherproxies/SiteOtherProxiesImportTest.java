package se.sead.siteotherproxies;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.sead.Application;
import se.sead.DataSourceFactory;
import se.sead.DefaultAccessDatabaseReader;
import se.sead.bugs.AccessReaderProvider;
import se.sead.bugsimport.site.seadmodel.SeadSite;
import se.sead.bugsimport.siteotherproxies.SiteOtherProxiesImporter;
import se.sead.bugsimport.siteotherproxies.bugsmodel.SiteOtherProxies;
import se.sead.bugsimport.siteotherproxies.seadmodel.SiteOtherRecord;
import se.sead.bugsimport.tracing.seadmodel.BugsError;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.model.TestEqualityHelper;
import se.sead.repositories.*;
import se.sead.testutils.DefaultConfig;

import javax.sql.DataSource;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration({Application.class, SiteOtherProxiesImportTest.Config.class})
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SiteOtherProxiesImportTest {

    @Configuration
    public static class Config extends DefaultConfig {

        public Config(){
            super("siteotherproxies", "siteotherproxies.mdb", "siteotherproxies.sql");
        }

        @Bean
        @Override
        public AccessReaderProvider getDatabaseReader() {

            return new DefaultAccessDatabaseReader(getMdbFile());
        }

        @Bean
        @Override
        public DataSource createDataSource() {
            return DataSourceFactory.createDefault(getDataFile());
        }
    }

    @Autowired
    private RecordTypeRepository recordTypeRepository;
    @Autowired
    private SiteRepository siteRepository;
    @Autowired
    private SiteOtherRecordRepository siteOtherRecordRepository;
    @Autowired
    private BugsTraceRepository traceRepository;
    @Autowired
    private BugsErrorRepository errorRepository;

    @Autowired
    private SiteOtherProxiesImporter importer;

    private ImportTestDefinition testDefinition;

    @Test
    public void runTest(){
        testDefinition = new ImportTestDefinition(recordTypeRepository, siteRepository);
        importer.run();
        verifyDatabaseContent();
        verifyTraceContent();
    }

    private TestEqualityHelper.ClassMethodInformation siteMethodInformation =
            new TestEqualityHelper.ClassMethodInformation(SeadSite.class, "getSiteLocations");

    private void verifyDatabaseContent() {
        List<SiteOtherRecord> expectedImportedResults = testDefinition.getExpectedImportedResults();
        List<SiteOtherRecord> actualImportedResults = getAllStored();
        SiteOtherRecordComparator comparator = new SiteOtherRecordComparator();
        assertEquals(expectedImportedResults.size(), actualImportedResults.size());
        Collections.sort(expectedImportedResults, comparator);
        Collections.sort(actualImportedResults, comparator);
        TestEqualityHelper<SiteOtherRecord> equalityHelper = new TestEqualityHelper<>(true);
        equalityHelper.addMethodInformation(siteMethodInformation);
        for (int i = 0; i < expectedImportedResults.size(); i++) {
            SiteOtherRecord expected = expectedImportedResults.get(i);
            SiteOtherRecord actual = actualImportedResults.get(i);
            try {
                if (expected.getId() == null) {
                    assertTrue(equalityHelper.equalsWithoutBlackListedMethods(expected, actual));
                } else {
                    assertEquals(expected, actual);
                }
            } catch (AssertionError ae){
                System.out.println(expected);
                System.out.println(actual);
                throw ae;
            }
        }
    }

    private List<SiteOtherRecord> getAllStored(){
        List<SeadSite> allSites = siteRepository.findAllByNameStartingWithIgnoreCase("");
        List<SiteOtherRecord> allStoredRecords = new ArrayList<>();
        for (SeadSite site:
             allSites) {
            allStoredRecords.addAll(siteOtherRecordRepository.findAllBySite(site));
        }
        return allStoredRecords;
    }

    private static class SiteOtherRecordComparator implements Comparator<SiteOtherRecord>{

        @Override
        public int compare(SiteOtherRecord o1, SiteOtherRecord o2) {
            return o1.getSite().compareTo(o2.getSite()) +
                    o1.getRecordType().getId().compareTo(o2.getRecordType().getId());
        }
    }

    private void verifyTraceContent(){
        for (SiteOtherProxies bugsData :
                SiteOtherProxiesExpectedBugsTableData.EXPECTED_DATA) {
            List<BugsTrace> traces = traceRepository.findByBugsTableAndCompressedBugsData(bugsData.bugsTable(), bugsData.compressToString());
            List<BugsError> errors = errorRepository.findByBugsTableAndCompressedBugsData(bugsData.bugsTable(), bugsData.compressToString());
            testDefinition.logCheck(bugsData, traces, errors);
        }

    }
}
