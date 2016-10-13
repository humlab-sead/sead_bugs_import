package se.sead.periods;

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
import se.sead.bugsimport.periods.PeriodImporter;
import se.sead.bugsimport.periods.bugsmodel.Period;
import se.sead.bugsimport.periods.seadmodel.RelativeAge;
import se.sead.repositories.*;
import se.sead.testutils.BugsTracesAndErrorsVerification;
import se.sead.testutils.DatabaseContentVerification;
import se.sead.testutils.DefaultConfig;

import javax.sql.DataSource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class, PeriodsImportTest.Config.class})
@TestConfiguration
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PeriodsImportTest {

    @Configuration
    public static class Config extends DefaultConfig {

        public Config(){
            super("periods", "periods.mdb", "periods.sql");
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
    private RelativeAgeRepository relativeAgeRepository;
    @Autowired
    private RelativeAgeTypeRepository relativeAgeTypeRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private BugsTraceRepository traceRepository;
    @Autowired
    private BugsErrorRepository errorRepository;

    @Autowired
    private PeriodImporter importer;

    @Test
    public void run(){
        DatabaseContentVerification<RelativeAge> databaseContentVerification = createDatabaseContentVerification();
        BugsTracesAndErrorsVerification<Period> logVerification = createLogVerification();
        importer.run();
        databaseContentVerification.verifyDatabaseContent();
        logVerification.verifyTraceContent();
    }

    private DatabaseContentVerification<RelativeAge> createDatabaseContentVerification(){
        return new DatabaseContentVerification<>(
                new DatabaseContentProvider(relativeAgeRepository, relativeAgeTypeRepository, locationRepository)
        );
    }

    private BugsTracesAndErrorsVerification<Period> createLogVerification(){
        return new BugsTracesAndErrorsVerification.ByIdentity<>(
                traceRepository,
                errorRepository,
                new LogVerificationCallback(),
                () -> ExpectedBugsData.EXPECTED_DATA
        );
    }
}
