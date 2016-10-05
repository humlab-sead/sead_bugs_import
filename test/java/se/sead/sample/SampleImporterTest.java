package se.sead.sample;

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
import se.sead.bugsimport.sample.SampleImporter;
import se.sead.bugsimport.sample.bugsmodel.BugsSample;
import se.sead.bugsimport.sample.seadmodel.Sample;
import se.sead.bugsimport.tracing.seadmodel.BugsError;
import se.sead.repositories.*;
import se.sead.testutils.BugsTracesAndErrorsVerification;
import se.sead.testutils.DatabaseContentVerification;
import se.sead.testutils.DefaultConfig;

import javax.sql.DataSource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class, SampleImporterTest.Config.class})
@TestConfiguration
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SampleImporterTest {


    @Configuration
    public static class Config extends DefaultConfig {

        public Config(){
            super("sample", "sample.mdb", "sample.sql");
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
    private SampleGroupRepository sampleGroupRepository;
    @Autowired
    private SampleTypeRepository sampleTypeRepository;
    @Autowired
    private AlternativeReferenceTypeRepository referenceTypeRepository;
    @Autowired
    private MethodRepository methodRepository;
    @Autowired
    private DimensionRepository dimensionRepository;
    @Autowired
    private BugsTraceRepository traceRepository;
    @Autowired
    private BugsErrorRepository errorRepository;

    @Autowired
    private SampleImporter importer;

    private DatabaseContentVerification<Sample> contentVerification;
    private BugsTracesAndErrorsVerification<BugsSample> logVerifier;

    @Test
    public void run(){
        createContentVerification();
        createLogVerification();
        importer.run();
        contentVerification.verifyDatabaseContent();
        logVerifier.verifyTraceContent();
    }

    private void createContentVerification(){
        contentVerification = new DatabaseContentVerification<>(
                new DatabaseContentProvider(
                        sampleRepository,
                        sampleGroupRepository,
                        sampleTypeRepository,
                        referenceTypeRepository,
                        methodRepository,
                        dimensionRepository
                ));
    }

    private void createLogVerification(){
        logVerifier = new BugsTracesAndErrorsVerification.ByIdentity<>(
                traceRepository,
                errorRepository,
                new LogVerification(),
                () -> ExpectedBugsData.EXPECTED_DATA
        );
    }
}
