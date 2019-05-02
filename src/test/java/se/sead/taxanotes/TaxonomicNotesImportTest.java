package se.sead.taxanotes;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.sead.bugsimport.taxanotes.TaxonomicNotesImporter;
import se.sead.bugsimport.taxanotes.bugsmodel.TaxoNotes;
import se.sead.bugsimport.taxanotes.seadmodel.TaxonomicNotes;
import se.sead.repositories.*;
import se.sead.testutils.BugsTracesAndErrorsVerification;
import se.sead.testutils.DatabaseContentVerification;
import se.sead.testutils.DefaultConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
public class TaxonomicNotesImportTest {

    @TestConfiguration
    public static class Config extends DefaultConfig{
        public Config(){
            super("taxanotes");
        }
    }

    @Autowired
    private TaxaOrderRepository orderRepository;
    @Autowired
    private TaxonomicNotesImporter importer;
    @Autowired
    private TaxonomicNotesRepository taxonomicNotesRepository;
    @Autowired
    private SpeciesRepository speciesRepository;
    @Autowired
    private BiblioDataRepository biblioDataRepository;
    @Autowired
    private BugsTraceRepository traceRepository;
    @Autowired
    private BugsErrorRepository errorRepository;

    private DatabaseContentVerification<TaxonomicNotes> databaseContentVerification;
    private BugsTracesAndErrorsVerification<TaxoNotes> logVerifier;

    @Before
    public void setup(){
        databaseContentVerification = new DatabaseContentVerification<>(
                new DatabaseContentProvider(
                        taxonomicNotesRepository,
                        biblioDataRepository,
                        speciesRepository
                )
        );
        logVerifier = new BugsTracesAndErrorsVerification.ByCompressed<>(
                traceRepository,
                errorRepository,
                new LogVerifier(),
                () -> ExpectedBugsData.EXPECTED_DATA
        );
    }

    @Test
    public void importTest(){
        importer.run();
        databaseContentVerification.verifyDatabaseContent();
        logVerifier.verifyTraceContent();
    }
}
