package se.sead.rdbsystems;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import se.sead.Application;
import se.sead.DataSourceFactory;
import se.sead.DefaultAccessDatabaseReader;
import se.sead.bugs.AccessReaderProvider;
import se.sead.bugsimport.rdbsystems.RdbSystemImporter;
import se.sead.bugsimport.rdbsystems.bugsmodel.BugsRDBSystem;
import se.sead.bugsimport.rdbsystems.seadmodel.RdbSystem;
import se.sead.repositories.*;
import se.sead.testutils.BugsTracesAndErrorsVerification;
import se.sead.testutils.DatabaseContentVerification;
import se.sead.testutils.DefaultConfig;

import javax.sql.DataSource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class, ImportDataTest.Config.class})
@TestConfiguration
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ImportDataTest {

    @Configuration
    public static class Config extends DefaultConfig {

        public Config(){
            super("rdbsystems", "rdbsystems.mdb", "rdbsystems.sql");
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
    private RDBSystemRepository rdbSystemRepository;
    @Autowired
    private BiblioDataRepository bibliographyRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private BugsTraceRepository traceRepository;
    @Autowired
    private BugsErrorRepository errorRepository;

    @Autowired
    private RdbSystemImporter importer;

    private RDBDatabaseContentTestDataProvider testDataProvider;
    private DatabaseContentVerification<RdbSystem> contentVerification;
    private BugsTracesAndErrorsVerification<BugsRDBSystem> tracesAndErrorsVerification;

    @Test
    public void run(){
        testDataProvider = new RDBDatabaseContentTestDataProvider(rdbSystemRepository, bibliographyRepository, locationRepository);
        contentVerification = new DatabaseContentVerification<>(testDataProvider);
        createTraceAndErrorVerifier();
        importer.run();
        contentVerification.verifyDatabaseContent();
        tracesAndErrorsVerification.verifyTraceContent();
    }

    private void createTraceAndErrorVerifier() {
        tracesAndErrorsVerification = new BugsTracesAndErrorsVerification.ByIdentity<>(
                traceRepository,
                errorRepository,
                new LogTraceAndErrorVerifier(),
                () -> ExpectedBugsData.EXPECTED_DATA
                );
    }

}
