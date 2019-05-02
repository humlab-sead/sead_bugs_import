package se.sead.country;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import se.sead.bugsimport.locations.country.CountryImporter;
import se.sead.bugsimport.locations.country.bugsmodel.Country;
import se.sead.bugsimport.locations.seadmodel.Location;
import se.sead.repositories.BugsErrorRepository;
import se.sead.repositories.BugsTraceRepository;
import se.sead.repositories.LocationRepository;
import se.sead.repositories.LocationTypeRepository;
import se.sead.testutils.BugsTracesAndErrorsVerification;
import se.sead.testutils.DatabaseContentVerification;
import se.sead.testutils.DefaultConfig;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
public class CountryImportTest {

    @TestConfiguration
    public static class Config extends DefaultConfig{
        public Config(){
            super("country", "countries.mdb", "countries.sql");
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
    @Autowired
    private BugsErrorRepository errorRepository;

    @Test
    public void doImport(){
        DatabaseContentVerification<Location> contentVerifier = createContentVerifier();
        BugsTracesAndErrorsVerification<Country> logVerifier = createLogVerifier();
        importer.run();
        contentVerifier.verifyDatabaseContent();
        logVerifier.verifyTraceContent();
    }

    private DatabaseContentVerification<Location> createContentVerifier(){
        return new DatabaseContentVerification<>(new CountryDatabaseContentProvider(locationTypeRepository, locationRepository));
    }

    private BugsTracesAndErrorsVerification<Country> createLogVerifier(){
        return new BugsTracesAndErrorsVerification.ByIdentity<>(
                traceRepository,
                errorRepository,
                new LogVerifier(),
                () -> ExpectedBugsData.EXPECTED_COUNTRIES
        );
    }

}
