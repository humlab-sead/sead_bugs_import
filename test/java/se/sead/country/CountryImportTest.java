package se.sead.country;

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
import se.sead.bugsimport.country.CountryImporter;
import se.sead.bugsimport.country.bugsmodel.Country;
import se.sead.bugsimport.country.seadmodel.Location;
import se.sead.bugsimport.country.seadmodel.LocationType;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.model.TestEqualityHelper;
import se.sead.repositories.BugsTraceRepository;
import se.sead.repositories.LocationRepository;
import se.sead.repositories.LocationTypeRepository;
import se.sead.util.DefaultConfig;

import javax.sql.DataSource;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration({Application.class, CountryImportTest.Config.class})
@ActiveProfiles("test")
public class CountryImportTest {

    @Configuration
    public static class Config extends DefaultConfig{
        public Config(){
            super("country", CountryExpectedTestDefinition.MDB_FILE, "countries.sql");
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
    private CountryImporter importer;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private LocationTypeRepository locationTypeRepository;
    @Autowired
    private BugsTraceRepository traceRepository;

    private CountryExpectedTestDefinition testDefinition = new CountryExpectedTestDefinition();

    @Test
    public void doImport(){
        importer.run();
        verifyContent();
        verifyTraces();
    }

    private void verifyContent() {
        LocationType countryType = locationTypeRepository.getCountryType();
        List<Location> actual = locationRepository.findByTypeOrderByName(countryType);
        List<Location> expectedResults = testDefinition.getExpectedResults(countryType);
        assertEquals(expectedResults.size(), actual.size());
        for (int i = 0; i < expectedResults.size(); i++) {
            assertTrue(TestEqualityHelper.equalsWithoutIdIfNeeded(expectedResults.get(i), actual.get(i)));
        }
    }

    private void verifyTraces(){
        for (Country bugsData :
                CountryExpectedTestDefinition.EXPECTED_COUNTRIES) {
            List<BugsTrace> traces = traceRepository.findByBugsTableAndCompressedBugsData("Country", bugsData.getCompressedStringBeforeTranslation());
            testDefinition.assertTraces(bugsData, traces);
        }
    }

}
