package se.sead.taxanotes;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.sead.AccessReaderTest;
import se.sead.Application;
import se.sead.DataSourceFactory;
import se.sead.DefaultAccessDatabaseReader;
import se.sead.bugs.AccessReaderProvider;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.bugsimport.taxanotes.TaxonomicNotesImporter;
import se.sead.bugsimport.taxanotes.bugsmodel.TaxoNotes;
import se.sead.bugsimport.taxanotes.seadmodel.TaxonomicNotes;
import se.sead.bugsimport.tracing.seadmodel.BugsError;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.model.TestEqualityHelper;
import se.sead.repositories.*;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration({Application.class, TaxonomicNotesImportTest.Config.class})
@ActiveProfiles("test")
public class TaxonomicNotesImportTest {

    @Configuration
    public static class Config {
        @Bean
        public AccessReaderProvider getDatabaseReader(){
            return new DefaultAccessDatabaseReader(
                    AccessReaderTest.RESOURCE_FOLDER +
                            "taxanotes/taxanotes.mdb"
            );
        }

        @Bean
        public DataSource createDataSource(){
            return DataSourceFactory.createDefault("taxanotes/taxanotes.sql");
        }
    }

    @Autowired
    private TaxaOrderRepository orderRepository;
    @Autowired
    private TaxonomicNotesImporter importer;
    @Autowired
    private TaxonomicNotesRepository taxonomicNotesRepository;
    @Autowired
    private SpeciesRepository speciesRepository;
    @Autowired
    private BugsTraceRepository traceRepository;
    @Autowired
    private BugsErrorRepository errorRepository;

    private BiblioBuilder biblioBuilder;
    private SpeciesBuilder speciesBuilder;
    private TaxaNotesImportTestDefinition testDefinition;

    @Before
    public void setup(){
        biblioBuilder = new BiblioBuilder();
        speciesBuilder = new SpeciesBuilder(orderRepository.getImportOrder());
        testDefinition = new TaxaNotesImportTestDefinition(speciesBuilder, biblioBuilder);
    }

    @Test
    public void importTest(){
        importer.run();
        verifyDatabaseContent();
        verifyTraces();
        verifyErrors();
    }

    private void verifyDatabaseContent(){
        List<TaxaSpecies> testSpecies = speciesBuilder.getAllSpecies();
        List<TaxonomicNotes> dbNotes = new ArrayList<>();
        for (TaxaSpecies species :
                testSpecies) {
            TaxaSpecies dbSpecies = speciesRepository.findByName(species.getSpeciesName(), species.getGenus().getGenusName());
            List<TaxonomicNotes> notesBySpecies = taxonomicNotesRepository.findBySpecies(dbSpecies);
            dbNotes.addAll(notesBySpecies);
        }
        compare(testDefinition.getExpectedNotes(), dbNotes);
    }

    private void compare(List<TaxonomicNotes> expectedNotes, List<TaxonomicNotes> actualNotes) {
        assertEquals(expectedNotes.size(), actualNotes.size());
        Comparator<TaxonomicNotes> testComparator = new TestTaxonomicNotesComparator();
        Collections.sort(expectedNotes, testComparator);
        Collections.sort(actualNotes, testComparator);
        for (int i = 0; i < expectedNotes.size(); i++) {
            assertTrue(TestEqualityHelper.equalsWithoutIdIfNeeded(expectedNotes.get(i), actualNotes.get(i)));
        }
    }

    private void verifyTraces(){
        for (TaxoNotes bugsData :
                TaxaNotesImportTestDefinition.EXPECTED_READ_ITEMS) {
            List<BugsTrace> traces = traceRepository.findByBugsTableAndCompressedBugsData("TTaxoNotes", bugsData.getCompressedStringBeforeTranslation());
            testDefinition.assertTrace(traces, bugsData);
        }
    }

    private void verifyErrors(){
        for (TaxoNotes bugsData :
                TaxaNotesImportTestDefinition.EXPECTED_READ_ITEMS) {
            List<BugsError> errors = errorRepository.findByBugsTableAndCompressedBugsData("TTaxoNotes", bugsData.getCompressedStringBeforeTranslation());
            testDefinition.assertErrors(errors, bugsData);
        }
    }

    private static class TestTaxonomicNotesComparator implements Comparator<TaxonomicNotes> {

        @Override
        public int compare(TaxonomicNotes o1, TaxonomicNotes o2) {
            int referenceDifference = o1.getReference().getId().compareTo(o2.getReference().getId());
            if(referenceDifference == 0){
                return o1.getNotes().compareTo(o2.getNotes());
            }
            return referenceDifference;
        }
    }
}
