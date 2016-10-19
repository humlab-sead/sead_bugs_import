package se.sead.fossil;

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
import se.sead.bugsimport.fossil.FossilImporter;
import se.sead.bugsimport.fossil.bugsmodel.Fossil;
import se.sead.bugsimport.fossil.seadmodel.Abundance;
import se.sead.repositories.*;
import se.sead.testutils.BugsTracesAndErrorsVerification;
import se.sead.testutils.DatabaseContentVerification;
import se.sead.testutils.DefaultConfig;

import javax.sql.DataSource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class, FossilImportTest.Config.class})
@TestConfiguration
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FossilImportTest {

    @Configuration
    public static class Config extends DefaultConfig {

        public Config(){
            super("fossil", "fossil.mdb", "fossil.sql");
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
    private SampleRepository sampleRepository;
    @Autowired
    private MethodRepository methodRepository;
    @Autowired
    private SpeciesRepository speciesRepository;
    @Autowired
    private DataTypeRepository dataTypeRepository;
    @Autowired
    private DatasetMasterRepository datasetMasterRepository;
    @Autowired
    private AbundanceRepository abundanceRepository;
    @Autowired
    private BugsTraceRepository traceRepository;
    @Autowired
    private BugsErrorRepository errorRepository;

    @Autowired
    private FossilImporter importer;

    @Test
    public void run(){
        DatabaseContentVerification<Abundance> databaseContentVerifier = createDatabaseContentVerifier();
        BugsTracesAndErrorsVerification<Fossil> logVerification = createLogVerification();
        importer.run();
        databaseContentVerifier.verifyDatabaseContent();
        logVerification.verifyTraceContent();
    }

    private DatabaseContentVerification<Abundance> createDatabaseContentVerifier(){
        return new DatabaseContentVerification<>(
              new DatabaseContentProvider(
                      sampleRepository,
                      speciesRepository,
                      dataTypeRepository,
                      methodRepository,
                      datasetMasterRepository,
                      abundanceRepository
                )
        );
    }

    private BugsTracesAndErrorsVerification<Fossil> createLogVerification(){
        return new BugsTracesAndErrorsVerification.ByIdentity<>(
                traceRepository,
                errorRepository,
                new LogVerification(),
                () -> ExpectedBugsData.EXPECTED_DATA
        );
    }


}
