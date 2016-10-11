package se.sead.datesradio;


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
import se.sead.bugsimport.datesradio.GeochronologyImporter;
import se.sead.bugsimport.datesradio.bugsmodel.DatesRadio;
import se.sead.bugsimport.datesradio.seadmodel.Geochronology;
import se.sead.repositories.*;
import se.sead.testutils.BugsTracesAndErrorsVerification;
import se.sead.testutils.DatabaseContentVerification;
import se.sead.testutils.DefaultConfig;

import javax.sql.DataSource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class, GeochronologyImportTest.Config.class})
@TestConfiguration
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class GeochronologyImportTest {


    @Configuration
    public static class Config extends DefaultConfig {

        public Config(){
            super("datesradio", "datesradio.mdb", "datesradio.sql");
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
    private DatasetMasterRepository datasetMasterRepository;
    @Autowired
    private DataTypeRepository dataTypeRepository;
    @Autowired
    private SampleRepository sampleRepository;
    @Autowired
    private MethodRepository methodRepository;
    @Autowired
    private GeochronologyRepository geochronologyRepository;
    @Autowired
    private DatingLabRepository datingLabRepository;
    @Autowired
    private DatingUncertaintyRepository datingUncertaintyRepository;
    @Autowired
    private BugsTraceRepository traceRepository;
    @Autowired
    private BugsErrorRepository errorRepository;

    @Autowired
    private GeochronologyImporter importer;

    @Test
    public void run(){
        DatabaseContentVerification<Geochronology> databaseContentVerification = createDatabaseContentVerification();
        BugsTracesAndErrorsVerification<DatesRadio> tracesAndErrorsVerification = createTracesAndErrorsVerification();
        importer.run();
        databaseContentVerification.verifyDatabaseContent();
        tracesAndErrorsVerification.verifyTraceContent();
    }

    private DatabaseContentVerification<Geochronology> createDatabaseContentVerification(){
        return new DatabaseContentVerification<>(new DatabaseContentProvider(
                datasetMasterRepository,
                dataTypeRepository,
                methodRepository,
                geochronologyRepository,
                datingLabRepository,
                sampleRepository,
                datingUncertaintyRepository
        ));
    }

    private BugsTracesAndErrorsVerification<DatesRadio> createTracesAndErrorsVerification(){
        return new BugsTracesAndErrorsVerification.ByIdentity<>(
                traceRepository,
                errorRepository,
                new LogAndErrorVerifier(),
                () -> ExpectedBugsData.EXPECTED_DATA
        );
    }
}
