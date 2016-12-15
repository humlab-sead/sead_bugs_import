package se.sead.sitelocations;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import se.sead.bugsimport.locations.seadmodel.Location;
import se.sead.bugsimport.sitelocations.SiteLocationImporter;
import se.sead.bugsimport.sitelocations.bugsmodel.BugsSiteLocation;
import se.sead.bugsimport.sitelocations.seadmodel.SiteLocation;
import se.sead.repositories.*;
import se.sead.testutils.BugsTracesAndErrorsVerification;
import se.sead.testutils.DatabaseContentVerification;
import se.sead.testutils.DefaultConfig;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
public class SiteLocationImportTest {

    @TestConfiguration
    public static class Config extends DefaultConfig {
        public Config(){
            super("sitelocations");
        }
    }

    @Autowired
    private SiteLocationImporter importer;
    @Autowired
    private SiteRepository siteRepository;
    @Autowired
    private SiteLocationRepository siteLocationRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private LocationTypeRepository locationTypeRepository;
    @Autowired
    private BugsTraceRepository traceRepository;
    @Autowired
    private BugsErrorRepository errorRepository;

    @Value("${allow.create.country:true}")
    private boolean canCreateCountry;

    private DatabaseContentVerification<SiteLocation> siteLocationDatabaseContentVerification;
    private DatabaseContentVerification<Location> locationDatabaseContentVerification;
    private BugsTracesAndErrorsVerification<BugsSiteLocation> logVerification;
    private ImportTestDefinition testDefinition;

    @Before
    public void setup(){
        testDefinition = new ImportTestDefinition(siteRepository, locationRepository, locationTypeRepository);
        siteLocationDatabaseContentVerification =
                new DatabaseContentVerification<>(
                        new SiteLocationContentVerificationDataProvider(
                                canCreateCountry,
                                testDefinition,
                                siteRepository,
                                siteLocationRepository
                        ));
        locationDatabaseContentVerification =
                new DatabaseContentVerification<>(
                        new LocationContentVerificationProvider(
                                canCreateCountry,
                                testDefinition,
                                locationRepository,
                                locationTypeRepository
                        )
                );
        logVerification = new BugsTracesAndErrorsVerification.ByCompressed<>(
                traceRepository,
                errorRepository,
                new SiteLocationTracesAndErrors(canCreateCountry),
                () -> ExpectedBugsData.EXPECTED_BUGS_DATA
        );
    }

    @Test
    public void run(){
        importer.run();
        siteLocationDatabaseContentVerification.verifyDatabaseContent();
        locationDatabaseContentVerification.verifyDatabaseContent();
        logVerification.verifyTraceContent();
    }
}
