package se.sead.rdb;

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
import se.sead.bugsimport.rdb.RdbImporter;
import se.sead.bugsimport.rdb.bugsmodel.BugsRDB;
import se.sead.bugsimport.rdb.seadmodel.Rdb;
import se.sead.rdbcodes.RdbCodeLogTraceAndErrorVerifier;
import se.sead.repositories.*;
import se.sead.testutils.BugsTracesAndErrorsVerification;
import se.sead.testutils.DatabaseContentVerification;
import se.sead.testutils.DefaultConfig;

import javax.sql.DataSource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class, ImportRDBTest.Config.class})
@TestConfiguration
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ImportRDBTest {


    @Configuration
    public static class Config extends DefaultConfig {

        public Config(){
            super("rdb", "rdb.mdb", "rdb.sql");
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
    private LocationRepository locationRepository;
    @Autowired
    private RdbRepository rdbRepository;
    @Autowired
    private RdbCodeRepository rdbCodeRepository;
    @Autowired
    private SpeciesRepository speciesRepository;
    @Autowired
    private BugsTraceRepository traceRepository;
    @Autowired
    private BugsErrorRepository errorRepository;

    @Autowired
    private RdbImporter importer;

    private BugsTracesAndErrorsVerification<BugsRDB> tracesAndErrorsVerification;
    private DatabaseContentVerification<Rdb> databaseContentVerification;


    @Test
    public void run(){
        databaseContentVerification = new DatabaseContentVerification<>(new RdbTestDataContent(locationRepository, speciesRepository, rdbCodeRepository, rdbRepository));
        tracesAndErrorsVerification = new BugsTracesAndErrorsVerification.ByCompressed<>(
                traceRepository,
                errorRepository,
                new RdbLogTraceAndErrorVerifier(),
                () -> ExpectedBugData.EXPECTED_DATA
        );
        importer.run();
        databaseContentVerification.verifyDatabaseContent();
        tracesAndErrorsVerification.verifyTraceContent();

    }
}
