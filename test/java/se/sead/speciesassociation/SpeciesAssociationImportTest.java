package se.sead.speciesassociation;

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
import se.sead.bugsimport.speciesassociation.SpeciesAssociationImporter;
import se.sead.bugsimport.speciesassociation.bugsmodel.BugsSpeciesAssociation;
import se.sead.bugsimport.speciesassociation.seadmodel.SpeciesAssociation;
import se.sead.repositories.*;
import se.sead.testutils.BugsTracesAndErrorsVerification;
import se.sead.testutils.DatabaseContentVerification;
import se.sead.testutils.DefaultConfig;

import javax.sql.DataSource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class, SpeciesAssociationImportTest.Config.class})
@TestConfiguration
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SpeciesAssociationImportTest {


    @Configuration
    public static class Config extends DefaultConfig {

        public Config(){
            super("speciesassociation", "speciesassociation.mdb", "speciesassociation.sql");
        }

        @Bean
        @Override
        public AccessReaderProvider getDatabaseReader() {
            return new DefaultAccessDatabaseReader(getMdbFile());
        }

        @Bean
        @Override
        public DataSource createDataSource() {
            return DataSourceFactory.createDefault(getDataFile());
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
