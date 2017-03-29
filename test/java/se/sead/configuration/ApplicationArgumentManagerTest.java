package se.sead.configuration;

import org.junit.Test;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.DefaultApplicationArguments;
import se.sead.bugsimport.Importer;
import se.sead.bugsimport.bibliography.BibliographyImporter;
import se.sead.bugsimport.locations.country.bugsmodel.Country;
import se.sead.bugsimport.locations.seadmodel.Location;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class ApplicationArgumentManagerTest {

    @Test
    public void shouldNotRunWhenProvidingNoRun(){
        ApplicationArguments arguments = createApplicationArguments("--no-run");
        ApplicationArgumentManager manager = new ApplicationArgumentManager(arguments);
        assertFalse(manager.shouldRun());
    }

    private DefaultApplicationArguments createApplicationArguments(String... arguments) {
        if(arguments == null){
            arguments = new String[0];
        }
        return new DefaultApplicationArguments(arguments);
    }

    @Test
    public void shouldNotRunWhenProvidingValidateSchema(){
        ApplicationArguments arguments = createApplicationArguments("--validate-schema");
        ApplicationArgumentManager manager = new ApplicationArgumentManager(arguments);
        assertFalse(manager.shouldRun());
    }

    @Test
    public void shouldRunWhenNotProvidingNoRunNorValidateSchema(){
        ApplicationArguments arguments = createApplicationArguments();
        ApplicationArgumentManager manager = new ApplicationArgumentManager(arguments);
        assertTrue(manager.shouldRun());
    }

    @Test
    public void getUnfilteredImporterListIfImportersOptionNotProvided(){
        ApplicationArguments arguments = createApplicationArguments();
        ApplicationArgumentManager manager = new ApplicationArgumentManager(arguments);
        List<Importer> importers = Arrays.asList(
                new MockImporter(),
                new MockImporter(),
                new MockImporter()
        );
        List<Importer> filteredImporters = manager.filter(importers);
        assertEquals(importers, filteredImporters);
    }

    @Test
    public void getFilteredImporterListIfImporterOptionProvidedOneFilter(){
        ApplicationArguments arguments = createApplicationArguments("--importers=MockFilter1");
        ApplicationArgumentManager manager = new ApplicationArgumentManager(arguments);
        List<Importer> importers = Arrays.asList(
                new MockImporter(),
                new MockImporter(),
                new MockImporter(),
                new MockFilter1Importer()
        );
        List<Importer> filteredImporters = manager.filter(importers);
        assertEquals(1, filteredImporters.size());
        assertEquals("MockFilter1Importer", filteredImporters.get(0).getClass().getSimpleName());
    }

    @Test
    public void getFilteredImporterListIfImporterOptionProvidedMultipleFilters(){
        ApplicationArguments arguments = createApplicationArguments("--importers=MockFilter1", "--importers=MockFilter2");
        ApplicationArgumentManager manager = new ApplicationArgumentManager(arguments);
        List<Importer> importers = Arrays.asList(
                new MockImporter(),
                new MockImporter(),
                new MockImporter(),
                new MockFilter1Importer(),
                new MockFilter2Importer()
        );
        List<Importer> filteredImporters = manager.filter(importers);
        List<String> simpleNames = filteredImporters.stream()
                .map(importer -> importer.getClass().getSimpleName())
                .sorted()
                .collect(Collectors.toList());

        assertEquals(2, simpleNames.size());
        assertEquals("MockFilter1Importer", simpleNames.get(0));
        assertEquals("MockFilter2Importer", simpleNames.get(1));
    }

    @Test
    public void getFilteredImporterListIfImporterOptionProvidedMultipleFiltersAlternativeNotation(){
        ApplicationArguments arguments = createApplicationArguments("--importers=MockFilter1,MockFilter2");
        ApplicationArgumentManager manager = new ApplicationArgumentManager(arguments);
        List<Importer> importers = Arrays.asList(
                new MockImporter(),
                new MockImporter(),
                new MockImporter(),
                new MockFilter1Importer(),
                new MockFilter2Importer()
        );
        List<Importer> filteredImporters = manager.filter(importers);
        List<String> simpleNames = filteredImporters.stream()
                .map(importer -> importer.getClass().getSimpleName())
                .sorted()
                .collect(Collectors.toList());

        assertEquals(2, simpleNames.size());
        assertEquals("MockFilter1Importer", simpleNames.get(0));
        assertEquals("MockFilter2Importer", simpleNames.get(1));
    }

    private static class MockImporter extends Importer<Country,Location> {

        MockImporter(){
            super(null, null);
        }

        @Override
        public void run() {
        }
    }

    private static class MockFilter1Importer extends Importer<Country,Location> {

        MockFilter1Importer(){
            super(null,null);
        }
    }

    private static class MockFilter2Importer extends Importer<Country, Location> {
        MockFilter2Importer(){
            super(null, null);
        }
    }
}