package se.sead.speciessynonyms;

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
import se.sead.bugsimport.speciesassociation.seadmodel.SpeciesAssociation;
import se.sead.bugsimport.speciessynonyms.SynonymImporter;
import se.sead.bugsimport.speciessynonyms.bugsmodel.Synonym;
import se.sead.repositories.*;
import se.sead.testutils.BugsTracesAndErrorsVerification;
import se.sead.testutils.DatabaseContentVerification;
import se.sead.testutils.DefaultConfig;

import javax.sql.DataSource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class, SynonymImportTest.Config.class})
@TestConfiguration
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SynonymImportTest {

    @Configuration
    public static class Config extends DefaultConfig {

        public Config(){
            super("speciessynonyms", "speciessynonyms.mdb", "speciessynonyms.sql");
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
    private TaxaGenusRepository genusRepository;
    @Autowired
    private TaxaFamilyRepository familyRepository;
    @Autowired
    private BiblioDataRepository biblioDataRepository;
    @Autowired
    private BugsTraceRepository traceRepository;
    @Autowired
    private BugsErrorRepository errorRepository;

    @Autowired
    private SynonymImporter importer;
    private LogVerifier logVerifier;

    @Test
    public void run(){
        DatabaseContentVerification<SpeciesAssociation> databaseContentVerifier = createDatabaseContentVerifier();
        BugsTracesAndErrorsVerification<Synonym> logVerification = createLogVerifier();
        importer.run();
        databaseContentVerifier.verifyDatabaseContent();
        logVerification.verifyTraceContent();
        logVerifier.assertOneAuthorCreated();
    }

    private DatabaseContentVerification<SpeciesAssociation> createDatabaseContentVerifier(){
        return new DatabaseContentVerification<>(new DatabaseContentProvider(
                speciesAssociationRepository,
                speciesAssociationTypeRepository,
                speciesRepository,
                genusRepository,
                familyRepository,
                biblioDataRepository
        ));
    }

    private BugsTracesAndErrorsVerification<Synonym> createLogVerifier(){
        logVerifier = new LogVerifier();
        return new BugsTracesAndErrorsVerification.ByCompressed<>(
                traceRepository,
                errorRepository,
                logVerifier,
                () -> ExpectedBugsData.EXPECTED_DATA
        );
    }
}
