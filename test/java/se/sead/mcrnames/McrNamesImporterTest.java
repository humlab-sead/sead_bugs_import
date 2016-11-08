package se.sead.mcrnames;


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
import se.sead.bugsimport.mcrnames.McrNamesImporter;
import se.sead.bugsimport.mcrnames.bugsmodel.BugsMCRNames;
import se.sead.bugsimport.mcrnames.seadmodel.MCRName;
import se.sead.repositories.BugsErrorRepository;
import se.sead.repositories.BugsTraceRepository;
import se.sead.repositories.McrNamesRepository;
import se.sead.testutils.BugsTracesAndErrorsVerification;
import se.sead.testutils.DatabaseContentVerification;
import se.sead.testutils.DefaultConfig;

import javax.sql.DataSource;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
public class McrNamesImporterTest {

    @TestConfiguration
    public static class Config extends DefaultConfig {
        public Config(){
            super("mcrnames");
        }
    }

    @Autowired
    private McrNamesRepository mcrNamesRepository;
    @Autowired
    private BugsTraceRepository traceRepository;
    @Autowired
    private BugsErrorRepository errorRepository;

    @Autowired
    private McrNamesImporter importer;

    private BugsTracesAndErrorsVerification<BugsMCRNames> tracesAndErrorVerifier;
    private DatabaseContentVerification<MCRName> databaseContentVerification;

    @Test
    public void run(){
        createDatabaseContentVerifer();
        createTracesAndErrorVerifier();
        importer.run();
        databaseContentVerification.verifyDatabaseContent();
        tracesAndErrorVerifier.verifyTraceContent();
    }

    private void createDatabaseContentVerifer() {
        databaseContentVerification = new DatabaseContentVerification<>(new DatabaseContentProvider(mcrNamesRepository));
    }

    private void createTracesAndErrorVerifier(){
        tracesAndErrorVerifier = new BugsTracesAndErrorsVerification.ByIdentity<>(
                traceRepository,
                errorRepository,
                new LogTracesAndErrorVerifier(),
                () -> ExpectedBugsData.EXPECTED_DATA
        );
    }
}
