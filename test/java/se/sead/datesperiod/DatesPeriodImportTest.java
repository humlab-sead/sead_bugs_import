package se.sead.datesperiod;

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
import se.sead.bugsimport.datesperiod.DatesPeriodImporter;
import se.sead.bugsimport.datesperiod.bugsmodel.DatesPeriod;
import se.sead.bugsimport.datesperiod.seadmodel.RelativeDate;
import se.sead.repositories.*;
import se.sead.testutils.BugsTracesAndErrorsVerification;
import se.sead.testutils.DatabaseContentVerification;
import se.sead.testutils.DefaultConfig;

import javax.sql.DataSource;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class, DatesPeriodImportTest.Config.class})
@TestConfiguration
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class DatesPeriodImportTest {

    @Configuration
    public static class Config extends DefaultConfig {

        public Config(){
            super("datesperiod", "datesperiod.mdb", "datesperiod.sql");
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
    private SampleRepository sampleRepository;
    @Autowired
    private MethodRepository methodRepository;
    @Autowired
    private DatingUncertaintyRepository datingUncertaintyRepository;
    @Autowired
    private RelativeAgeRepository relativeAgeRepository;
    @Autowired
    private RelativeDateRepository relativeDateRepository;
    @Autowired
    private BugsTraceRepository traceRepository;
    @Autowired
    private BugsErrorRepository errorRepository;

    @Autowired
    private DatesPeriodImporter importer;


    @Test
    public void run(){
        DatabaseContentVerification<RelativeDate> databaseContentVerification = createDatabaseContentVerification();
        BugsTracesAndErrorsVerification<DatesPeriod> traceVerification = createTraceVerification();
        importer.run();
        databaseContentVerification.verifyDatabaseContent();
        traceVerification.verifyTraceContent();
    }

    private DatabaseContentVerification<RelativeDate> createDatabaseContentVerification(){
        return new DatabaseContentVerification<>(new DatabaseContentProvider(
                sampleRepository,
                datingUncertaintyRepository,
                methodRepository,
                relativeAgeRepository,
                relativeDateRepository
        ));
    }

    private BugsTracesAndErrorsVerification<DatesPeriod> createTraceVerification(){
        return new BugsTracesAndErrorsVerification.ByIdentity<>(
                traceRepository,
                errorRepository,
                new LogVerification(),
                () -> ExpectedBugsData.EXPECTED_DATA
        );
    }
}
