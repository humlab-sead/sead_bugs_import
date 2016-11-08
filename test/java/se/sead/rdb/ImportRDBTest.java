package se.sead.rdb;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import se.sead.bugsimport.rdb.RdbImporter;
import se.sead.bugsimport.rdb.bugsmodel.BugsRDB;
import se.sead.bugsimport.rdb.seadmodel.Rdb;
import se.sead.repositories.*;
import se.sead.testutils.BugsTracesAndErrorsVerification;
import se.sead.testutils.DatabaseContentVerification;
import se.sead.testutils.DefaultConfig;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
public class ImportRDBTest {

    @TestConfiguration
    public static class Config extends DefaultConfig {
        public Config(){
            super("rdb");
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
