package se.sead.rdbcodes;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import se.sead.bugsimport.rdbcodes.RdbCodeImporter;
import se.sead.bugsimport.rdbcodes.bugsmodel.BugsRDBCodes;
import se.sead.repositories.BugsErrorRepository;
import se.sead.repositories.BugsTraceRepository;
import se.sead.repositories.RDBSystemRepository;
import se.sead.repositories.RdbCodeRepository;
import se.sead.testutils.BugsTracesAndErrorsVerification;
import se.sead.testutils.DatabaseContentVerification;
import se.sead.testutils.DefaultConfig;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
public class RdbCodesImportTest {

    @TestConfiguration
    public static class Config extends DefaultConfig {
        public Config(){
            super("rdbcodes");
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
