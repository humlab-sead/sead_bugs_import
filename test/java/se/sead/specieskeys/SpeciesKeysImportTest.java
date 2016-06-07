package se.sead.specieskeys;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.sead.*;
import se.sead.bugs.AccessReaderProvider;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.bugsimport.specieskeys.IdentificationKeysImporter;
import se.sead.bugsimport.specieskeys.bugsmodel.Keys;
import se.sead.bugsimport.specieskeys.seadmodel.TextIdentificationKeys;
import se.sead.bugsimport.tracing.seadmodel.BugsError;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.repositories.*;
import se.sead.model.TestEqualityHelper;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration({Application.class, SpeciesKeysImportTest.Config.class})
@ActiveProfiles("test")
public class SpeciesKeysImportTest {

    @Configuration
    public static class Config implements ApplicationConfiguration {

        @Bean
        public AccessReaderProvider getDatabaseReader(){
            return new DefaultAccessDatabaseReader(
                    AccessReaderTest.RESOURCE_FOLDER +
                            "specieskeys/specieskeys.mdb"
            );
        }

        @Bean
        public DataSource createDataSource(){
            return DataSourceFactory.createDefault("specieskeys/specieskeys.sql");
        }
    }

    @Autowired
    private TaxaOrderRepository orderRepository;
    @Autowired
    private IdentificationKeysImporter importer;
    @Autowired
    private SpeciesRepository speciesRepository;
    @Autowired
    private TextIdentificationKeysRepository identificationKeysRepository;
    @Autowired
    private BugsTraceRepository traceRepository;
    @Autowired
    private BugsErrorRepository errorRepository;

    private SpeciesBuilder speciesBuilder;
    private KeysTestDefinition testDefinition;

    @Before
    public void setup(){
        speciesBuilder = new SpeciesBuilder(orderRepository.getImportOrder());
        testDefinition = new KeysTestDefinition(speciesBuilder);
    }

    @Test
    public void importSpeciesKeys(){
        importer.run();
        verifyDatabaseContent();
        verifyTraces();
    }

    private void verifyDatabaseContent() {
        List<TextIdentificationKeys> keys = new ArrayList<>();
        for (TaxaSpecies species :
                speciesBuilder.getSpecies()) {
            TaxaSpecies dbSpecies = speciesRepository.findByName(species.getSpeciesName(), species.getGenus().getGenusName());
            keys.addAll(identificationKeysRepository.findBySpecies(dbSpecies));
        }
        compare(testDefinition.getExpectedResults(), keys);
    }

    private void compare(List<TextIdentificationKeys> expectedResults, List<TextIdentificationKeys> actualResults){
        assertEquals(expectedResults.size(), actualResults.size());
        for (int i = 0; i < expectedResults.size(); i++) {
            assertTrue(TestEqualityHelper.equalsWithoutIdIfNeeded(expectedResults.get(i), actualResults.get(i)));
        }
    }

    private void verifyTraces() {
        for (Keys bugsData :
                KeysTestDefinition.EXPECTED_READ_ITEMS) {
            List<BugsTrace> traces = traceRepository.findByBugsTableAndCompressedBugsData("TKeys", bugsData.compressToString());
            testDefinition.assertTracces(traces, bugsData);
        }
    }

    private void verifyErrors(){
        for (Keys bugsData:
                KeysTestDefinition.EXPECTED_READ_ITEMS){
            List<BugsError> errors = errorRepository.findByBugsTableAndCompressedBugsData("TKeys", bugsData.compressToString());
            testDefinition.assertErrors(errors, bugsData);
        }
    }
}
