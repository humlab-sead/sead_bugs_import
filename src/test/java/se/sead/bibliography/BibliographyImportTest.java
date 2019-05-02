package se.sead.bibliography;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import se.sead.bugsimport.bibliography.BibliographyImporter;
import se.sead.bugsimport.bibliography.bugsmodel.BugsBiblio;
import se.sead.bugsimport.bibliography.seadmodel.Biblio;
import se.sead.repositories.BiblioDataRepository;
import se.sead.repositories.BugsErrorRepository;
import se.sead.repositories.BugsTraceRepository;
import se.sead.testutils.BugsTracesAndErrorsVerification;
import se.sead.testutils.DatabaseContentVerification;
import se.sead.testutils.DefaultConfig;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
public class BibliographyImportTest {

    @TestConfiguration
    public static class Config extends DefaultConfig {
        public Config(){
            super("bibliography", "bibliography.mdb", "bibliography.sql");
        }
    }


    @Autowired
    private BibliographyImporter importer;
    @Autowired
    private BiblioDataRepository biblioDataRepository;
    @Autowired
    private BugsTraceRepository traceRepository;
    @Autowired
    private BugsErrorRepository errorRepository;

    private DatabaseContentVerification<Biblio> contentVerification;
    private BugsTracesAndErrorsVerification<BugsBiblio> logVerification;

    @Before
    public void setup(){
        contentVerification = new DatabaseContentVerification<>(
                new BibliographyDatabaseContentProvider(biblioDataRepository)
        );
        logVerification = new BugsTracesAndErrorsVerification.ByIdentity<>(
              traceRepository,
                errorRepository,
                new LogVerifier(),
                () -> ExpectedBugsData.EXPECTED_DATA
        );
    }

    @Test
    public void run(){
        importer.run();
        contentVerification.verifyDatabaseContent();
        logVerification.verifyTraceContent();
    }

}
