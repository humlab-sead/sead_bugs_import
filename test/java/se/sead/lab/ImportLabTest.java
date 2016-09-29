package se.sead.lab;

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
import se.sead.bugsimport.attributes.bugsmodel.BugsAttributes;
import se.sead.bugsimport.attributes.seadmodel.TaxaMeasuredAttributes;
import se.sead.bugsimport.lab.LabImporter;
import se.sead.bugsimport.lab.bugsmodel.BugsLab;
import se.sead.bugsimport.lab.seadmodel.DatingLab;
import se.sead.repositories.BugsErrorRepository;
import se.sead.repositories.BugsTraceRepository;
import se.sead.repositories.DatingLabRepository;
import se.sead.repositories.LocationRepository;
import se.sead.testutils.BugsTracesAndErrorsVerification;
import se.sead.testutils.DatabaseContentVerification;
import se.sead.testutils.DefaultConfig;

import javax.sql.DataSource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class, ImportLabTest.Config.class})
@TestConfiguration
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ImportLabTest {

    @Configuration
    public static class Config extends DefaultConfig {

        public Config(){
            super("lab", "lab.mdb", "lab.sql");
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
    private LocationRepository locationRepository;
    @Autowired
    private DatingLabRepository datingLabRepository;
    @Autowired
    private BugsTraceRepository traceRepository;
    @Autowired
    private BugsErrorRepository errorRepository;

    @Autowired
    private LabImporter importer;

    private DatabaseContentVerification<DatingLab> databaseContentVerification;
    private BugsTracesAndErrorsVerification<BugsLab> tracesAndErrorsVerification;

    @Test
    public void run(){
        createContentVerification();
        createTraceAndLogVerification();
        importer.run();
        databaseContentVerification.verifyDatabaseContent();
        tracesAndErrorsVerification.verifyTraceContent();
    }

    private void createContentVerification(){
        databaseContentVerification = new DatabaseContentVerification<>(
                new DatabaseContentProvider(locationRepository, datingLabRepository));
    }

    private void createTraceAndLogVerification(){
        tracesAndErrorsVerification =
                new BugsTracesAndErrorsVerification.ByIdentity<>(
                        traceRepository,
                        errorRepository,
                        new TracesAndErrorsVerifier(),
                        () -> ExpectedBugsData.EXPECTED_DATA
                );
    }
}
