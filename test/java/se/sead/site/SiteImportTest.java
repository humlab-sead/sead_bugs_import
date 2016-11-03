package se.sead.site;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.sead.Application;
import se.sead.DataSourceFactory;
import se.sead.DefaultAccessDatabaseReader;
import se.sead.bugs.AccessReaderProvider;
import se.sead.bugsimport.locations.seadmodel.Location;
import se.sead.bugsimport.site.SiteImporter;
import se.sead.bugsimport.site.bugsmodel.BugsSite;
import se.sead.bugsimport.site.seadmodel.SeadSite;
import se.sead.bugsimport.sitelocations.seadmodel.SiteLocation;
import se.sead.bugsimport.tracing.seadmodel.BugsError;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.model.TestEqualityHelper;
import se.sead.repositories.*;
import se.sead.testutils.DefaultConfig;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes={Application.class, SiteImportTestNoUpdatesOrCreationOfCountries.Config.class})
@TestConfiguration
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public abstract class SiteImportTest {
    @Autowired
    private SiteImporter importer;
    @Autowired
    private LocationTypeRepository typeRepository;
    @Autowired
    private SiteRepository siteRepository;
    @Autowired
    private BugsTraceRepository traceRepository;
    @Autowired
    private BugsErrorRepository errorRepository;
    @Autowired
    private TypeTranslationRepository typeTranslationRepository;
    @Value("${allow.create.country}")
    private Boolean canCreateCountry;
    @Value("${allow.site.updates}")
    private Boolean allowSiteUpdates;
    private SiteTestDefinition testDefinition;

    public abstract void runImporter();

    protected void doTest(){
        testDefinition = new SiteTestDefinition(canCreateCountry, allowSiteUpdates);
        importer.run();
        verifyDatabaseContent();
        verifyTraces();
    }

    private void verifyDatabaseContent() {
        List<SeadSite> expectedResults = testDefinition.getExpectedResults();
        List<SeadSite> actualResults = siteRepository.findAllByNameStartingWithIgnoreCase("");
        assertEquals(expectedResults.size(), actualResults.size());
        Collections.sort(expectedResults);
        Collections.sort(actualResults);
        for (int i = 0; i < expectedResults.size(); i++) {
            try {
                assertEachField(expectedResults.get(i), actualResults.get(i));
            } catch (AssertionError ae){
                System.out.println(expectedResults.get(i));
                System.out.println(actualResults.get(i));
                throw ae;
            }
        }
    }

    private void assertEachField(SeadSite expected, SeadSite actual){
        if(expected.getAltitude() == null){
            assertNull(actual.getAltitude());
        } else {
            assertEquals(0, expected.getAltitude().compareTo(actual.getAltitude()));
        }
        if(expected.getDescription() == null){
            assertNull(actual.getDescription());
        } else {
            String expectedDescription = expected.getDescription().replace("\r\n", "\n");
            String actualDescription = actual.getDescription().replace("\r\n", "\n");
            assertEquals(expectedDescription, actualDescription);
        }
        if(expected.getLatitude() == null){
            assertNull(actual.getLatitude());
        } else {
            assertEquals(0, expected.getLatitude().compareTo(actual.getLatitude()));
        }
        if(expected.getLongitude() == null){
            assertNull(actual.getLongitude());
        } else {
            assertEquals(0, expected.getLongitude().compareTo(actual.getLongitude()));
        }
        assertEquals(expected.getName(), actual.getName());
        if(expected.getNationalSiteIdentifier() == null){
            assertNull(actual.getNationalSiteIdentifier());
        } else {
            assertEquals(expected.getNationalSiteIdentifier(), actual.getNationalSiteIdentifier());
        }
        //assertLocations(expected.getSiteLocations(), actual.getSiteLocations());
    }

    private void assertLocations(List<SiteLocation> expectedSiteLocations, List<SiteLocation> actualSiteLocations){
        if(expectedSiteLocations == null){
            assertNull(actualSiteLocations);
        } else {
            assertEquals(expectedSiteLocations.size(), actualSiteLocations.size());
            List<Location> expectedLocation = extractLocations(expectedSiteLocations);
            List<Location> actualLocations = extractLocations(actualSiteLocations);
            Collections.sort(expectedLocation);
            Collections.sort(actualLocations);
            TestEqualityHelper<Location> locationEqualityTester = new TestEqualityHelper<>();
            for (int i = 0; i < expectedLocation.size(); i++) {
                assertTrue(locationEqualityTester.equalsWithoutBlackListedMethods(expectedLocation.get(i), actualLocations.get(i)));
            }
        }
    }

    private List<Location> extractLocations(List<SiteLocation> siteLocations){
        return siteLocations.stream().map(siteLocation -> siteLocation.getLocation())
                .collect(Collectors.toList());
    }

    private void verifyTraces(){
        SiteTracesResults traceResultsAnalysis = new SiteTracesResults(canCreateCountry, allowSiteUpdates);
        for (BugsSite bugsVersion:
             SiteTestDefinition.EXPECTED_BUGS_DATA) {
            List<BugsTrace> traces = traceRepository.findByBugsTableAndBugsIdentifierOrderByChangeDate("TSite", bugsVersion.getBugsIdentifier());
            List<BugsError> errors = errorRepository.findByBugsTableAndBugsIdentifier("TSite", bugsVersion.getBugsIdentifier());
            traceResultsAnalysis.assertTracesForBugsData(bugsVersion, traces, errors);
        }
    }

    @Configuration
    public static class Config extends DefaultConfig {

        public Config(){
            super("site", "site.mdb", "site.sql");
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
}
