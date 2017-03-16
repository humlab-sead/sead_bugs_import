package se.sead.datesperiod;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import se.sead.bugsimport.datesperiod.DatesPeriodImporter;
import se.sead.bugsimport.datesperiod.bugsmodel.DatesPeriod;
import se.sead.bugsimport.datesperiod.seadmodel.RelativeDate;
import se.sead.repositories.*;
import se.sead.testutils.BugsTracesAndErrorsVerification;
import se.sead.testutils.DatabaseContentVerification;
import se.sead.testutils.DefaultConfig;
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
public class DatesPeriodImportTest {

    @TestConfiguration
    public static class Config extends DefaultConfig {

        public Config(){
            super("datesperiod");
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
    private DatasetMasterRepository datasetMasterRepository;
    @Autowired
    private DataTypeRepository dataTypeRepository;
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
                relativeDateRepository,
                datasetMasterRepository,
                dataTypeRepository
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
