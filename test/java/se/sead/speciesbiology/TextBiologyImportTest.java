package se.sead.speciesbiology;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.bugsimport.speciesbiology.TextBiologyImporter;
import se.sead.bugsimport.speciesbiology.bugsmodel.Biology;
import se.sead.bugsimport.speciesbiology.seadmodel.TextBiology;
import se.sead.bugsimport.tracing.seadmodel.BugsError;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.model.TestEqualityHelper;
import se.sead.repositories.*;
import se.sead.testutils.DefaultConfig;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
public class TextBiologyImportTest {

    @TestConfiguration
    public static class Config extends DefaultConfig{
        public Config(){
            super("speciesbiology",SpeciesBiologyTestDefinition.ACCESS_DATABASE_FILE, "biology.sql");
        }
    }

    @Autowired
    private TextBiologyImporter importer;
    @Autowired
    private TextBiologyDataRepository biologyDataRepository;
    @Autowired
    private TaxaOrderRepository orderRepository;
    @Autowired
    private SpeciesRepository speciesRepository;
    @Autowired
    private BugsTraceRepository traceRepository;
    @Autowired
    private BugsErrorRepository errorRepository;

    private SpeciesBiologyTestDefinition testDefinition;

    @Test
    public void biologyImport(){
        testDefinition = new SpeciesBiologyTestDefinition(orderRepository.getImportOrder());
        importer.run();
        verifyInsertedData();
        verifyTraces();
    }

    private void verifyInsertedData() {
        TextBiologyBuilder expectedResultBuilder = testDefinition.getBiologyBuilder();
        for (TaxaSpecies expectedSpecies :
                testDefinition.getSpeciesBuilder().getSpecies()) {
            TaxaSpecies dbLoadedSpecies = speciesRepository.findByName(expectedSpecies.getSpeciesName(), expectedSpecies.getGenus().getGenusName());
            List<TextBiology> biologyTexts = biologyDataRepository.findBySpecies(dbLoadedSpecies);
            List<TextBiology> expectedBiology = expectedResultBuilder.getExpectedSeadData(expectedSpecies);
            compare(expectedBiology, biologyTexts);
        }
    }

    private void compare(List<TextBiology> expected, List<TextBiology> actual){
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < actual.size(); i++) {
            assertTrue(TestEqualityHelper.equalsWithoutIdIfNeeded(expected.get(i), actual.get(i)));
        }
    }

    private void verifyTraces(){
        for (Biology bugsData :
                SpeciesBiologyTestDefinition.EXPECTED_ACCESS_DATA) {
            List<BugsTrace> traces = traceRepository.findByBugsTableAndCompressedBugsData("TBiology", bugsData.getCompressedStringBeforeTranslation());
            assertTraces(bugsData, traces);
        }
    }

    private void assertTraces(Biology bugsData, List<BugsTrace> traces) {
        if(bugsData.getCode() == 1.001002d) {
            assertTrue(traces.isEmpty());
        } else if(bugsData.getRef().equals("Böcher (1995)")){
            verifyErrorExist(bugsData);
        } else {
            assertFalse(traces.isEmpty());
            assertTrue(
                    traces.stream()
                        .map(trace -> trace.getSeadTable())
                        .allMatch(tableName -> "tbl_text_biology".equals(tableName)));
        }
    }

    private void verifyErrorExist(Biology bugsData) {
        List<BugsError> errors = errorRepository.findByBugsTableAndCompressedBugsData("TBiology", bugsData.getCompressedStringBeforeTranslation());
        assertEquals(1, errors.size());
        BugsError error = errors.get(0);
        assertEquals("No reference found for reference: Böcher (1995)", error.getMessage());
    }
}
