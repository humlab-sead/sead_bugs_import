package se.sead.rdbcodes;

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
import se.sead.bugsimport.rdbcodes.RdbCodeImporter;
import se.sead.bugsimport.rdbcodes.bugsmodel.BugsRDBCodes;
import se.sead.repositories.BugsErrorRepository;
import se.sead.repositories.BugsTraceRepository;
import se.sead.repositories.RDBSystemRepository;
import se.sead.repositories.RdbCodeRepository;
import se.sead.testutils.BugsTracesAndErrorsVerification;
import se.sead.testutils.DatabaseContentVerification;
import se.sead.testutils.DefaultConfig;

import javax.sql.DataSource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class, RdbCodesImportTest.Config.class})
@TestConfiguration
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RdbCodesImportTest {


    @Configuration
    public static class Config extends DefaultConfig {

        public Config(){
            super("rdbcodes", "rdbcodes.mdb", "rdbcodes.sql");
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
    private RdbCodeRepository rdbCodeRepository;
    @Autowired
    private RDBSystemRepository rdbSystemRepository;
    @Autowired
    private BugsTraceRepository traceRepository;
    @Autowired
    private BugsErrorRepository errorRepository;

    @Autowired
    private RdbCodeImporter importer;


    private DatabaseContentVerification contentVerifier;
    private BugsTracesAndErrorsVerification<BugsRDBCodes> logTraceAndErrorVerifier;

    @Test
    public void run(){
        contentVerifier = new DatabaseContentVerification(new RDBCodesContentTestDataProvider(rdbCodeRepository, rdbSystemRepository));
        logTraceAndErrorVerifier = new BugsTracesAndErrorsVerification.ByIdentity<>(traceRepository, errorRepository, new RdbCodeLogTraceAndErrorVerifier(), () -> ExpectedBugsData.EXPECTED_DATA);
        importer.run();
        contentVerifier.verifyDatabaseContent();
        logTraceAndErrorVerifier.verifyTraceContent();
    }
}
