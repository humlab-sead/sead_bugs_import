package se.sead.mcr;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import se.sead.Application;
import se.sead.DataSourceFactory;
import se.sead.DefaultAccessDatabaseReader;
import se.sead.bugs.AccessReaderProvider;
import se.sead.bugsimport.mcr.MCRSummaryImporter;
import se.sead.bugsimport.mcr.bugsmodel.MCRSummaryData;
import se.sead.bugsimport.mcr.seadmodel.MCRSummary;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.bugsimport.tracing.seadmodel.BugsError;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.model.TestEqualityHelper;
import se.sead.repositories.*;
import se.sead.species.TestSpeciesProvider;
import se.sead.testutils.DefaultConfig;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
public class MCRSummaryImportTest {

    @TestConfiguration
    public static class Config extends DefaultConfig{
        public Config(){
            super("mcr", "mcrsummary.mdb", "mcrsummary.sql");
        }
    }

    @Autowired
    private TaxaOrderRepository orderRepository;
    @Autowired
    private SpeciesRepository speciesRepository;
    @Autowired
    private MCRSummaryRepository summaryRepository;
    @Autowired
    private BugsTraceRepository traceRepository;
    @Autowired
    private BugsErrorRepository errorRepository;

    @Autowired
    private MCRSummaryImporter importer;

    private MCRSummarTestDefinition testDefinition;
    private TestSpeciesProvider speciesProvider;

    @Before
    public void setup(){
        speciesProvider = new TestSpeciesProvider(new MCRSummaryTestSpeciesTreeBuilder(orderRepository.getImportOrder()));
        testDefinition = new MCRSummarTestDefinition(speciesProvider);
    }

    @Test
    public void runImport(){
        importer.run();
        verifyDatabaseContent();
        verifyTraces();
        verifyErrors();
    }

    private void verifyDatabaseContent() {
        List<MCRSummary> actualContent = new ArrayList<>();
        for (TaxaSpecies dbSpecies :
                getDbBackedSpecies()) {
             actualContent.add(summaryRepository.findBySpecies(dbSpecies));
        }
        compare(testDefinition.getExpectedResults(), actualContent);
    }

    private List<TaxaSpecies> getDbBackedSpecies(){
        return speciesProvider.getSpecies().stream()
                .map(species -> speciesRepository.findByName(species.getSpeciesName(), species.getGenus().getGenusName()))
                .collect(Collectors.toList());
    }

    private void compare(List<MCRSummary> expectedResults, List<MCRSummary> actualResults){
        assertEquals(expectedResults.size(), actualResults.size());
        for (int i = 0; i < expectedResults.size(); i++) {
            assertTrue(TestEqualityHelper.equalsWithoutIdIfNeeded(expectedResults.get(i), actualResults.get(i)));
        }
    }

    private void verifyTraces() {
        for (MCRSummaryData bugsData :
                MCRSummarTestDefinition.EXPECTED_BUGS_DATA) {
            List<BugsTrace> traces = traceRepository.findByBugsTableAndCompressedBugsData("TMCRSummaryData", bugsData.getCompressedStringBeforeTranslation());
            testDefinition.assertTraces(traces, bugsData);
        }
    }

    private void verifyErrors() {
        for (MCRSummaryData bugsData:
                MCRSummarTestDefinition.EXPECTED_BUGS_DATA){
            List<BugsError> errors = errorRepository.findByBugsTableAndCompressedBugsData("TMCRSummaryData", bugsData.getCompressedStringBeforeTranslation());
            testDefinition.assertErrors(errors, bugsData);
        }
    }

}
