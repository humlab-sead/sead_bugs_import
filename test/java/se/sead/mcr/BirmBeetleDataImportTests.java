package se.sead.mcr;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.sead.Application;
import se.sead.DataSourceFactory;
import se.sead.DefaultAccessDatabaseReader;
import se.sead.bugs.AccessReaderProvider;
import se.sead.bugsimport.mcr.bugsmodel.BirmBeetleDat;
import se.sead.bugsimport.mcr.BirmBeetleDataImporter;
import se.sead.bugsimport.mcr.seadmodel.BirmBeetleData;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.bugsimport.tracing.seadmodel.BugsError;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.repositories.*;
import se.sead.model.TestEqualityHelper;
import se.sead.util.DefaultConfig;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration({Application.class, BirmBeetleDataImportTests.Config.class})
@ActiveProfiles("test")
public class BirmBeetleDataImportTests {

    @Configuration
    public static class Config extends DefaultConfig{

        public Config(){
            super("mcr", BirmBeetleTestDefinition.BUGS_DATA_FILE, "birmbeetledata.sql");
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
    private BirmBeetleDataImporter importer;
    @Autowired
    private TaxaOrderRepository orderRepository;
    @Autowired
    private SpeciesRepository speciesRepository;
    @Autowired
    private BirmBeetleDataRepository mcrRepository;
    @Autowired
    private BugsTraceRepository traceRepository;
    @Autowired
    private BugsErrorRepository errorRepository;
    private BirmBeetleTestDefinition testDefinition;

    @Before
    public void setup(){
        testDefinition = new BirmBeetleTestDefinition(orderRepository.getImportOrder());
    }

    @Test
    public void birmBeetleDataImport(){
        importer.run();
        verifyDatabaseContent();
        verifyTraces();
    }

    private void verifyDatabaseContent(){
        BirmBeetleTestDefinition.SeadBirmBeetleDataBuilder builder = testDefinition.getBuilder();
        verifyExistingData(builder);
        verifyInsertedData(builder);
    }

    private void verifyExistingData(BirmBeetleTestDefinition.SeadBirmBeetleDataBuilder builder) {
        TaxaSpecies existingSpecies = builder.getExistingSpecies();
        List<BirmBeetleData> result = mcrRepository.findBySpecies(existingSpecies);
        Collections.sort(result);
        List<BirmBeetleData> existingMCRData = builder.createExistingMCRData();
        Collections.sort(existingMCRData);
        assertEquals(existingMCRData, result);
    }

    private void verifyInsertedData(BirmBeetleTestDefinition.SeadBirmBeetleDataBuilder builder) {
        TaxaSpecies savedSpecies = readCreatedSpeciesFromDb(builder);
        List<BirmBeetleData> bySpecies = mcrRepository.findBySpecies(savedSpecies);
        Collections.sort(bySpecies);
        List<BirmBeetleData> expectedData = builder.createNewData();
        Collections.sort(expectedData);
        assertEquals(expectedData.size(), bySpecies.size());
        for (int i = 0; i < bySpecies.size(); i++) {
            BirmBeetleData read = bySpecies.get(i);
            BirmBeetleData expected = expectedData.get(i);
            assertTrue(TestEqualityHelper.equalsWithoutIdIfNeeded(read, expected));
        }
    }

    private TaxaSpecies readCreatedSpeciesFromDb(BirmBeetleTestDefinition.SeadBirmBeetleDataBuilder builder) {
        TaxaSpecies createdSpecies = builder.getCreatedSpecies();
        return speciesRepository.findByName(createdSpecies.getSpeciesName(), createdSpecies.getGenus().getGenusName());
    }

    private void verifyTraces(){
        for (BirmBeetleDat bugsData:
                (List<BirmBeetleDat>) BirmBeetleTestDefinition.EXPECTED_ROW_DATA) {
            List<BugsTrace> traces = traceRepository.findByBugsTableAndCompressedBugsData("TbirmBEETLEdat", bugsData.getCompressedStringBeforeTranslation());
            if(bugsData.getBugsCode() == 1.0010070d) {
                List<String> addedTableReferences = traces.stream()
                        .map(trace -> trace.getSeadTable())
                        .distinct()
                        .collect(Collectors.toList());
                assertEquals(Arrays.asList("tbl_mcrdata_birmbeetledat"), addedTableReferences);
            } else if(bugsData.getBugsCode() == 2.0000000d){
                checkErrorLogs(bugsData);
                assertTrue(traces.isEmpty());
            } else {
                assertTrue(traces.isEmpty());
            }
        }
    }

    private void checkErrorLogs(BirmBeetleDat bugsData){
        List<BugsError> errors = errorRepository.findByBugsTableAndCompressedBugsData("TbirmBEETLEdat", bugsData.getCompressedStringBeforeTranslation());
        assertEquals(1, errors.size());
        BugsError bugsError = errors.get(0);
        assertEquals("Species not found in SEAD, not imported? bugs_code = 2,0000000", bugsError.getMessage());
    }
}
