package se.sead.speciesbiology;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import se.sead.bugsimport.speciesbiology.TextBiologyImporter;
import se.sead.bugsimport.speciesbiology.bugsmodel.Biology;
import se.sead.bugsimport.speciesbiology.seadmodel.TextBiology;
import se.sead.repositories.*;
import se.sead.testutils.BugsTracesAndErrorsVerification;
import se.sead.testutils.DatabaseContentVerification;
import se.sead.testutils.DefaultConfig;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
public class TextBiologyImportTest {

    @TestConfiguration
    public static class Config extends DefaultConfig{
        public Config(){
            super("speciesbiology", "biology.mdb", "biology.sql");
        }
    }

    @Autowired
    private TextBiologyImporter importer;
    @Autowired
    private TextBiologyDataRepository biologyDataRepository;
    @Autowired
    private SpeciesRepository speciesRepository;
    @Autowired
    private BiblioDataRepository biblioDataRepository;
    @Autowired
    private BugsTraceRepository traceRepository;
    @Autowired
    private BugsErrorRepository errorRepository;

    private DatabaseContentVerification<TextBiology> databaseContentVerification;
    private BugsTracesAndErrorsVerification<Biology> logVerifier;

    @Before
    public void setup(){
        databaseContentVerification = new DatabaseContentVerification<>(
                new DatabaseContentProvider(biologyDataRepository, speciesRepository, biblioDataRepository)
        );
        logVerifier = new BugsTracesAndErrorsVerification.ByCompressed<>(
                traceRepository,
                errorRepository,
                new LogVerifier(),
                () -> ExpectedBugsData.EXPECTED_DATA
        );
    }

    @Test
    public void biologyImport(){
        importer.run();
        databaseContentVerification.verifyDatabaseContent();
        logVerifier.verifyTraceContent();
    }
}
