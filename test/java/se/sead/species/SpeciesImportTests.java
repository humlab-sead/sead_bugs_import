package se.sead.species;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import se.sead.bugsimport.species.IndexImporter;
import se.sead.bugsimport.species.bugsmodel.INDEX;
import se.sead.bugsimport.species.seadmodel.TaxonomicOrder;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.repositories.*;
import se.sead.testutils.BugsTracesAndErrorsVerification;
import se.sead.testutils.DatabaseContentVerification;
import se.sead.testutils.DefaultConfig;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
public class SpeciesImportTests {

    @TestConfiguration
    public static class Config extends DefaultConfig {
        public Config(){
            super("species", "INDEX.mdb", "species.sql");
        }
    }

    @Autowired
    private IndexImporter importer;

    @Autowired
    private TaxonomicOrderRepository taxonomicOrderRepository;
    @Autowired
    private TaxonomicOrderSystemRepository taxonomicOrderSystemRepository;
    @Autowired
    private TaxaOrderRepository orderRepository;
    @Autowired
    private BugsTraceRepository traceRepository;
    @Autowired
    private BugsErrorRepository errorRepository;
    @Autowired
    private SpeciesRepository speciesRepository;
    @Autowired
    private TaxaGenusRepository genusRepository;
    @Autowired
    private TaxaFamilyRepository familyRepository;

    private DatabaseContentVerification<TaxonomicOrder> databaseContentVerification;
    private BugsTracesAndErrorsVerification<INDEX> logVerifier;

    @Before
    public void setup(){
        databaseContentVerification = new DatabaseContentVerification<>(
                new DatabaseContentProvider(
                        speciesRepository,
                        genusRepository,
                        familyRepository,
                        orderRepository,
                        taxonomicOrderRepository,
                        taxonomicOrderSystemRepository
                )
        );
        logVerifier = new BugsTracesAndErrorsVerification.ByIdentity<>(
                traceRepository,
                errorRepository,
                new LogVerifier(),
                () -> ExpectedBugsData.EXPECTED_ROW_DATA
        );
    }

    @Test
    public void INDEXimport(){
        importer.run();
        databaseContentVerification.verifyDatabaseContent();
        logVerifier.verifyTraceContent();
    }
}