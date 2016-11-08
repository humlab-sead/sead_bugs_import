package se.sead.ecocodedefinitiongroups;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import se.sead.bugsimport.ecocodedefinitiongroups.EcocodeGroupImporter;
import se.sead.bugsimport.ecocodedefinitiongroups.bugsmodel.EcoDefGroups;
import se.sead.bugsimport.ecocodedefinitiongroups.seadmodel.EcocodeGroup;
import se.sead.repositories.BugsErrorRepository;
import se.sead.repositories.BugsTraceRepository;
import se.sead.repositories.EcocodeGroupRepository;
import se.sead.repositories.EcocodeSystemRepository;
import se.sead.testutils.BugsTracesAndErrorsVerification;
import se.sead.testutils.DatabaseContentVerification;
import se.sead.testutils.DefaultConfig;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
public class EcocodeDefinitionGroupImportTest {

    @TestConfiguration
    public static class Config extends DefaultConfig {
        public Config(){
            super("ecocodedefinitiongroups");
        }
    }

    @Autowired
    private EcocodeGroupRepository ecocodeGroupRepository;
    @Autowired
    private EcocodeSystemRepository ecocodeSystemRepository;
    @Autowired
    private BugsTraceRepository traceRepository;
    @Autowired
    private BugsErrorRepository errorRepository;

    @Autowired
    private EcocodeGroupImporter importer;


    @Test
    public void run(){
        DatabaseContentVerification<EcocodeGroup> databaseContentVerifier = createDatabaseContentVerifier();
        BugsTracesAndErrorsVerification<EcoDefGroups> logVerifier = createLogVerifier();
        importer.run();
        databaseContentVerifier.verifyDatabaseContent();
        logVerifier.verifyTraceContent();
    }

    private DatabaseContentVerification<EcocodeGroup> createDatabaseContentVerifier(){
        return new DatabaseContentVerification<>(
                new DatabaseContentProvider(
                        ecocodeGroupRepository,
                        ecocodeSystemRepository
                )
        );
    }

    private BugsTracesAndErrorsVerification<EcoDefGroups> createLogVerifier(){
        return new BugsTracesAndErrorsVerification.ByIdentity<>(
                traceRepository,
                errorRepository,
                new LogVerification(),
                () -> BugsExpectedData.EXPECTED_ACCESS_DATA
        );
    }
}
