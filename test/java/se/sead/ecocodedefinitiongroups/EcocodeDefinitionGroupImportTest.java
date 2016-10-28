package se.sead.ecocodedefinitiongroups;

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
import se.sead.bugsimport.ecocodedefinitiongroups.EcocodeGroupImporter;
import se.sead.bugsimport.ecocodedefinitiongroups.bugsmodel.EcoDefGroups;
import se.sead.bugsimport.ecocodedefinitiongroups.seadmodel.EcocodeGroup;
import se.sead.ecocodedefinition.ExpectedBugsAccessData;
import se.sead.repositories.BugsErrorRepository;
import se.sead.repositories.BugsTraceRepository;
import se.sead.repositories.EcocodeGroupRepository;
import se.sead.repositories.EcocodeSystemRepository;
import se.sead.testutils.BugsTracesAndErrorsVerification;
import se.sead.testutils.DatabaseContentVerification;
import se.sead.testutils.DefaultConfig;

import javax.sql.DataSource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class, EcocodeDefinitionGroupImportTest.Config.class})
@TestConfiguration
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class EcocodeDefinitionGroupImportTest {


    @Configuration
    public static class Config extends DefaultConfig {

        public Config(){
            super("ecocodedefinitiongroups", "ecocodedefinitiongroups.mdb", "ecocodedefinitiongroups.sql");
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
