package se.sead.speciesassociation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import se.sead.bugsimport.speciesassociation.SpeciesAssociationImporter;
import se.sead.bugsimport.speciesassociation.bugsmodel.BugsSpeciesAssociation;
import se.sead.bugsimport.speciesassociation.seadmodel.SpeciesAssociation;
import se.sead.repositories.*;
import se.sead.testutils.BugsTracesAndErrorsVerification;
import se.sead.testutils.DatabaseContentVerification;
import se.sead.testutils.DefaultConfig;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
public class SpeciesAssociationImportTest {

    @TestConfiguration
    public static class Config extends DefaultConfig {
        public Config(){
            super("speciesassociation");
        }
    }

    @Autowired
    private SpeciesAssociationRepository speciesAssociationRepository;
    @Autowired
    private SpeciesAssociationTypeRepository speciesAssociationTypeRepository;
    @Autowired
    private SpeciesRepository speciesRepository;
    @Autowired
    private BiblioDataRepository biblioDataRepository;
    @Autowired
    private BugsTraceRepository traceRepository;
    @Autowired
    private BugsErrorRepository errorRepository;

    @Autowired
    private SpeciesAssociationImporter importer;

    @Test
    public void run(){
        DatabaseContentVerification<SpeciesAssociation> databaseContentVerification = createDatabaseContentVerification();
        BugsTracesAndErrorsVerification<BugsSpeciesAssociation> logVerifier = createLogVerifier();
        importer.run();
        databaseContentVerification.verifyDatabaseContent();
        logVerifier.verifyTraceContent();
    }

    private DatabaseContentVerification<SpeciesAssociation> createDatabaseContentVerification(){
        return new DatabaseContentVerification<>(
                new DatabaseContentProvider(
                        speciesAssociationRepository,
                        speciesAssociationTypeRepository,
                        biblioDataRepository,
                        speciesRepository
                )
        );
    }

    private BugsTracesAndErrorsVerification<BugsSpeciesAssociation> createLogVerifier(){
        return new BugsTracesAndErrorsVerification.ByIdentity<>(
                traceRepository,
                errorRepository,
                new LogVerifier(),
                () -> ExpectedBugsData.EXPECTED_DATA
        );
    }

}
