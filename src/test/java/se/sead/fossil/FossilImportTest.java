package se.sead.fossil;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import se.sead.Application;
import se.sead.bugsimport.fossil.FossilImporter;
import se.sead.bugsimport.fossil.bugsmodel.Fossil;
import se.sead.sead.data.Abundance;
import se.sead.repositories.*;
import se.sead.sead.data.AnalysisEntity;
import se.sead.sead.data.Dataset;
import se.sead.testutils.BugsTracesAndErrorsVerification;
import se.sead.testutils.DatabaseContentVerification;
import se.sead.testutils.DefaultConfig;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={Application.class, FossilImportTest.Config.class})
@ActiveProfiles("test")
@DirtiesContext
public abstract class FossilImportTest {

    @TestConfiguration
    public static class Config extends DefaultConfig {
        public Config(){
            super("fossil");
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
    private DatasetRepository datasetRepository;
    @Autowired
    private AnalysisEntityRepository analysisEntityRepository;
    @Value("${allow.dataset.updates}")
    private Boolean allowDatasetUpdates;

    @Autowired
    private FossilImporter importer;

    private DatabaseContentVerification<Abundance> abundanceDatabaseContentVerifier;
    private DatabaseContentVerification<Dataset> datasetDatabaseContentVerifier;
    private DatabaseContentVerification<AnalysisEntity> analysisEntityDatabaseContentVerifier;
    private BugsTracesAndErrorsVerification<Fossil> abundanceLogVerification;

    @Before
    public final void setup(){
        abundanceDatabaseContentVerifier = new DatabaseContentVerification<>(
                new AbundanceDatabaseContentProvider(
                        sampleRepository,
                        speciesRepository,
                        dataTypeRepository,
                        methodRepository,
                        datasetMasterRepository,
                        abundanceRepository,
                        allowDatasetUpdates
                )
        );
        datasetDatabaseContentVerifier = new DatabaseContentVerification<>(
                new DatasetDatabaseContentProvider(
                    dataTypeRepository,
                        methodRepository,
                        datasetMasterRepository,
                        datasetRepository,
                        allowDatasetUpdates
                )
        );
        analysisEntityDatabaseContentVerifier = new DatabaseContentVerification<>(
                new AnalysisEntityDatabaseContentProvider(
                        sampleRepository,
                        datasetMasterRepository,
                        dataTypeRepository,
                        methodRepository,
                        analysisEntityRepository,
                        allowDatasetUpdates
                )
        );
        abundanceLogVerification = new BugsTracesAndErrorsVerification.ByIdentity<>(
                traceRepository,
                errorRepository,
                new LogVerification(allowDatasetUpdates),
                () -> ExpectedBugsData.EXPECTED_DATA
        );

    }

    public abstract void test();

    public final void run(){
        importer.run();
        abundanceDatabaseContentVerifier.verifyDatabaseContent();
        analysisEntityDatabaseContentVerifier.verifyDatabaseContent();
        datasetDatabaseContentVerifier.verifyDatabaseContent();
        abundanceLogVerification.verifyTraceContent();
    }

}
