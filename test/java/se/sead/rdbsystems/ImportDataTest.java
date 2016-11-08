package se.sead.rdbsystems;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import se.sead.bugsimport.rdbsystems.RdbSystemImporter;
import se.sead.bugsimport.rdbsystems.bugsmodel.BugsRDBSystem;
import se.sead.bugsimport.rdbsystems.seadmodel.RdbSystem;
import se.sead.repositories.*;
import se.sead.testutils.BugsTracesAndErrorsVerification;
import se.sead.testutils.DatabaseContentVerification;
import se.sead.testutils.DefaultConfig;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
public class ImportDataTest {

    @TestConfiguration
    public static class Config extends DefaultConfig {
        public Config(){
            super("rdbsystems");
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
