package se.sead.datescalendar;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import se.sead.bugsimport.datescalendar.DatesCalendarImporter;
import se.sead.bugsimport.datescalendar.bugsmodel.DatesCalendar;
import se.sead.bugsimport.datesperiod.seadmodel.RelativeDate;
import se.sead.bugsimport.periods.seadmodel.RelativeAge;
import se.sead.repositories.*;
import se.sead.testutils.BugsTracesAndErrorsVerification;
import se.sead.testutils.DatabaseContentVerification;
import se.sead.testutils.DefaultConfig;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
public class DatesCalendarImportTest {

    @TestConfiguration
    public static class Config extends DefaultConfig {

        public Config(){
            super("datescalendar");
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
    private DatasetMasterRepository datasetMasterRepository;
    @Autowired
    private DataTypeRepository dataTypeRepository;

    @Autowired
    private DatesCalendarImporter importer;

    @Test
    @Ignore
    public void run(){
        DatabaseContentVerification<RelativeDate> relativeDatesDatabaseContentVerifier = createDatabaseContentVerifier();
        DatabaseContentVerification<RelativeAge> ageDatabaseContentVerifier = createAgeDatabaseContentVerifier();
        BugsTracesAndErrorsVerification<DatesCalendar> logVerifier = createLogVerifier();
        importer.run();
        relativeDatesDatabaseContentVerifier.verifyDatabaseContent();
        ageDatabaseContentVerifier.verifyDatabaseContent();
        logVerifier.verifyTraceContent();
    }

    private DatabaseContentVerification<RelativeDate> createDatabaseContentVerifier(){
        return new DatabaseContentVerification<>(new RelativeDatesDatabaseContentProvider(
                sampleRepository,
                datingUncertaintyRepository,
                methodRepository,
                relativeAgeRepository,
                relativeAgeTypeRepository,
                relativeDateRepository,
                datasetMasterRepository,
                dataTypeRepository
        ));
    }

    private DatabaseContentVerification<RelativeAge> createAgeDatabaseContentVerifier(){
        return new DatabaseContentVerification<>(
                new RelativeAgesDatabaseContentProvider(relativeAgeTypeRepository, relativeAgeRepository)
        );
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
