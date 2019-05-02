package se.sead.specieskeys;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import se.sead.bugsimport.specieskeys.IdentificationKeysImporter;
import se.sead.bugsimport.specieskeys.bugsmodel.Keys;
import se.sead.bugsimport.specieskeys.seadmodel.TextIdentificationKeys;
import se.sead.repositories.BugsErrorRepository;
import se.sead.repositories.BugsTraceRepository;
import se.sead.repositories.TaxaOrderRepository;
import se.sead.repositories.TextIdentificationKeysRepository;
import se.sead.testutils.BugsTracesAndErrorsVerification;
import se.sead.testutils.DatabaseContentVerification;
import se.sead.testutils.DefaultConfig;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
public class SpeciesKeysImportTest {

    @TestConfiguration
    public static class Config extends DefaultConfig {

        public Config(){
            super("specieskeys");
        }
    }

    @Autowired
    private TaxaOrderRepository orderRepository;
    @Autowired
    private IdentificationKeysImporter importer;
    @Autowired
    private TextIdentificationKeysRepository identificationKeysRepository;
    @Autowired
    private BugsTraceRepository traceRepository;
    @Autowired
    private BugsErrorRepository errorRepository;

    @Test
    public void importSpeciesKeys(){
        DatabaseContentVerification<TextIdentificationKeys> databaseContentVerifier = createDatabaseContentVerifier();
        BugsTracesAndErrorsVerification<Keys> logVerifier = createLogVerifier();
        importer.run();
        databaseContentVerifier.verifyDatabaseContent();
        logVerifier.verifyTraceContent();
    }

    private DatabaseContentVerification<TextIdentificationKeys> createDatabaseContentVerifier() {
        return new DatabaseContentVerification<>(
                new DatabaseContentVerifier(orderRepository, identificationKeysRepository)
        );
    }

    private BugsTracesAndErrorsVerification<Keys> createLogVerifier(){
        return new BugsTracesAndErrorsVerification.ByCompressed<>(
                traceRepository,
                errorRepository,
                new LogVerifier(),
                () -> ExpectedBugsData.EXPECTED_READ_ITEMS
        );
    }

}
