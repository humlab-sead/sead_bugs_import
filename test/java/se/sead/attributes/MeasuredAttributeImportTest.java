package se.sead.attributes;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import se.sead.bugsimport.attributes.TaxaMeasuredAttributesImporter;
import se.sead.bugsimport.attributes.bugsmodel.BugsAttributes;
import se.sead.bugsimport.attributes.seadmodel.TaxaMeasuredAttributes;
import se.sead.repositories.BugsErrorRepository;
import se.sead.repositories.BugsTraceRepository;
import se.sead.repositories.MeasuredAttributesRepository;
import se.sead.repositories.SpeciesRepository;
import se.sead.testutils.BugsTracesAndErrorsVerification;
import se.sead.testutils.DatabaseContentVerification;
import se.sead.testutils.DefaultConfig;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
@DirtiesContext
public class MeasuredAttributeImportTest  {

    @TestConfiguration
    public static class Config extends DefaultConfig {
        public Config(){
            super("attributes");
        }
    }

    @Autowired
    private SpeciesRepository speciesRepository;
    @Autowired
    private MeasuredAttributesRepository measuredAttributesRepository;
    @Autowired
    private BugsTraceRepository traceRepository;
    @Autowired
    private BugsErrorRepository errorRepository;

    @Autowired
    private TaxaMeasuredAttributesImporter importer;

    private DatabaseContentVerification<TaxaMeasuredAttributes> contentVerification;
    private BugsTracesAndErrorsVerification<BugsAttributes> tracesAndErrorsVerification;

    @Test
    public void run(){
        createContentVerification();
        createTracesAndErrorsVerification();
        importer.run();
        contentVerification.verifyDatabaseContent();
        tracesAndErrorsVerification.verifyTraceContent();
    }

    private void createContentVerification(){
        contentVerification = new DatabaseContentVerification<>(new DatabaseContentProvider(speciesRepository, measuredAttributesRepository));
    }

    private void createTracesAndErrorsVerification(){
        tracesAndErrorsVerification = new BugsTracesAndErrorsVerification.ByCompressed<>(
                traceRepository,
                errorRepository,
                new TracesAndErrorVerifier(),
                () -> ExpectedBugsData.EXPECTED_DATA
        );
    }
}
