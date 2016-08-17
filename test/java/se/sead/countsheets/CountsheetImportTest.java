package se.sead.countsheets;

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
import se.sead.bugsimport.countsheets.SampleGroupImporter;
import se.sead.bugsimport.countsheets.bugsmodel.Countsheet;
import se.sead.bugsimport.countsheets.seadmodel.SampleGroup;
import se.sead.bugsimport.site.seadmodel.SeadSite;
import se.sead.model.TestEqualityHelper;
import se.sead.repositories.*;
import se.sead.testutils.BugsTracesAndErrorsVerification;
import se.sead.testutils.DatabaseContentVerification;
import se.sead.testutils.DefaultConfig;

import javax.sql.DataSource;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration({Application.class, CountsheetImportTest.Config.class})
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CountsheetImportTest {

    @Configuration
    public static class Config extends DefaultConfig {

        public Config(){
            super("countsheets", "countsheets.mdb", "countsheets.sql");
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
    private SiteRepository siteRepository;
    @Autowired
    private SamplingContextRepository contextRepository;
    @Autowired
    private MethodRepository methodRepository;
    @Autowired
    private SampleGroupImporter importer;
    @Autowired
    private SampleGroupRepository sampleGroupRepository;
    @Autowired
    private BugsTraceRepository traceRepository;
    @Autowired
    private BugsErrorRepository errorRepository;

    private ImportTestDefinition testDefinition;
    private DatabaseContentVerification<SampleGroup> databaseContentVerification;
    private BugsTracesAndErrorsVerification<Countsheet> logContentVerification;

    @Test
    public void run(){
        testDefinition = new ImportTestDefinition(contextRepository, siteRepository, methodRepository);
        setupDatabaseVerification();
        setupLogVerification();
        importer.run();
        verifyDatabaseContent();
        verifyImportLogs();
    }

    private void setupDatabaseVerification() {
        databaseContentVerification = new DatabaseContentVerification<>(new SampleGroupDatabaseContentProvider(testDefinition, sampleGroupRepository));
    }

    private void setupLogVerification() {
        logContentVerification = new BugsTracesAndErrorsVerification<>(
                traceRepository,
                errorRepository,
                (bugsData, traces, errors) -> testDefinition.checkTracesAndErrors(bugsData, traces, errors),
                () -> ExpectedBugsData.EXPECTED_DATA);
    }

    private void verifyDatabaseContent() {
        databaseContentVerification.verifyDatabaseContent();
    }

    private void verifyImportLogs(){
        logContentVerification.verifyTraceContent();
    }

    private static class SampleGroupDatabaseContentProvider implements DatabaseContentVerification.DatabaseContentTestDataProvider<SampleGroup> {

        private ImportTestDefinition testDefinition;
        private SampleGroupRepository sampleGroupRepository;

        public SampleGroupDatabaseContentProvider(ImportTestDefinition testDefinition, SampleGroupRepository sampleGroupRepository) {
            this.testDefinition = testDefinition;
            this.sampleGroupRepository = sampleGroupRepository;
        }

        @Override
        public List<SampleGroup> getExpectedData() {
            return testDefinition.getExpectedSeadData();
        }

        @Override
        public List<SampleGroup> getActualData() {
            return sampleGroupRepository.findAll();
        }

        @Override
        public Comparator<SampleGroup> getSorter() {
            return new SampleGroupComparator();
        }

        @Override
        public TestEqualityHelper<SampleGroup> getEqualityHelper() {
            TestEqualityHelper.ClassMethodInformation siteMethodInformation =
                    new TestEqualityHelper.ClassMethodInformation(SeadSite.class, "getSiteLocations");
            TestEqualityHelper<SampleGroup> equalityHelper = new TestEqualityHelper<>(true);
            equalityHelper.addMethodInformation(siteMethodInformation);
            return equalityHelper;
        }
    }

    private static class SampleGroupComparator implements Comparator<SampleGroup> {
        @Override
        public int compare(SampleGroup o1, SampleGroup o2) {
            String o1Data = o1.getName() + o1.getSite().getName();
            String o2Data = o2.getName() + o2.getSite().getName();
            return o1Data.compareTo(o2Data);

        }
    }
}
