package se.sead.datescalendar;

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
import se.sead.bugsimport.datescalendar.DatesCalendarImporter;
import se.sead.bugsimport.datescalendar.bugsmodel.DatesCalendar;
import se.sead.bugsimport.datesperiod.seadmodel.RelativeDate;
import se.sead.repositories.*;
import se.sead.testutils.BugsTracesAndErrorsVerification;
import se.sead.testutils.DatabaseContentVerification;
import se.sead.testutils.DefaultConfig;

import javax.sql.DataSource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class, DatesCalendarImportTest.Config.class})
@TestConfiguration
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class DatesCalendarImportTest {

    @Configuration
    public static class Config extends DefaultConfig {

        public Config(){
            super("datescalendar", "datescalendar.mdb", "datescalendar.sql");
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
    private DatingUncertaintyRepository datingUncertaintyRepository;
    @Autowired
    private MethodRepository methodRepository;
    @Autowired
    private RelativeAgeRepository relativeAgeRepository;
    @Autowired
    private RelativeAgeTypeRepository relativeAgeTypeRepository;
    @Autowired
    private RelativeDateRepository relativeDateRepository;
    @Autowired
    private BugsTraceRepository traceRepository;
    @Autowired
    private BugsErrorRepository errorRepository;

    @Autowired
    private DatesCalendarImporter importer;

    @Test
    public void run(){
        DatabaseContentVerification<RelativeDate> databaseContentVerifier = createDatabaseContentVerifier();
        BugsTracesAndErrorsVerification<DatesCalendar> logVerifier = createLogVerifier();
        importer.run();
        databaseContentVerifier.verifyDatabaseContent();
        logVerifier.verifyTraceContent();
    }

    private DatabaseContentVerification<RelativeDate> createDatabaseContentVerifier(){
        return new DatabaseContentVerification<>(new DatabaseContentProvider(
                sampleRepository,
                datingUncertaintyRepository,
                methodRepository,
                relativeAgeRepository,
                relativeAgeTypeRepository,
                relativeDateRepository
        ));
    }

    private BugsTracesAndErrorsVerification<DatesCalendar> createLogVerifier(){
        return new BugsTracesAndErrorsVerification.ByIdentity<>(
                traceRepository,
                errorRepository,
                new LogVerifier(),
                () -> ExpectedBugsData.EXPECTED_DATA
        );
    }
}
