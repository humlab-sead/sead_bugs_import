package se.sead.sitelocations;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import se.sead.bugsimport.site.seadmodel.SeadSite;
import se.sead.bugsimport.sitelocations.SiteLocationImporter;
import se.sead.bugsimport.sitelocations.bugsmodel.BugsSiteLocation;
import se.sead.bugsimport.sitelocations.seadmodel.SiteLocation;
import se.sead.model.TestEqualityHelper;
import se.sead.repositories.*;
import se.sead.testutils.BugsTracesAndErrorsVerification;
import se.sead.testutils.DatabaseContentVerification;
import se.sead.testutils.DefaultConfig;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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

    private DatabaseContentVerification<SiteLocation> contentVerification;
    private BugsTracesAndErrorsVerification<BugsSiteLocation> logVerification;
    private ImportTestDefinition testDefinition;

    @Test
    public void run(){
        testDefinition = new ImportTestDefinition(siteRepository, locationRepository, locationTypeRepository);
        createContentVerificationData();
        createLogVerification();
        importer.run();
        contentVerification.verifyDatabaseContent();
        logVerification.verifyTraceContent();
    }

    private void createContentVerificationData() {
        contentVerification =
                new DatabaseContentVerification<>(
                        new SiteLocationContentVerificationDataProvider(
                                canCreateCountry,
                                testDefinition,
                                siteRepository,
                                siteLocationRepository
                        ));
    }

    private void createLogVerification(){
        logVerification = new BugsTracesAndErrorsVerification.ByCompressed<>(
            traceRepository,
            errorRepository,
            new SiteLocationTracesAndErrors(canCreateCountry),
            () -> ExpectedBugsData.EXPECTED_BUGS_DATA
        );
    }

    private static class SiteLocationContentVerificationDataProvider implements DatabaseContentVerification.DatabaseContentTestDataProvider<SiteLocation> {

        private boolean canCreateCountry;
        private ImportTestDefinition testDefinition;
        private SiteRepository siteRepository;
        private SiteLocationRepository siteLocationRepository;

        public SiteLocationContentVerificationDataProvider(boolean canCreateCountry, ImportTestDefinition testDefinition, SiteRepository siteRepository, SiteLocationRepository siteLocationRepository) {
            this.canCreateCountry = canCreateCountry;
            this.testDefinition = testDefinition;
            this.siteRepository = siteRepository;
            this.siteLocationRepository = siteLocationRepository;
        }

        @Override
        public List<SiteLocation> getExpectedData() {
            return testDefinition.getExpectedData(canCreateCountry);
        }

        @Override
        public List<SiteLocation> getActualData() {
            List<SiteLocation> allStored = new ArrayList<>();
            for (SeadSite site :
                    siteRepository.findAllByNameStartingWithIgnoreCase("")) {
                allStored.addAll(siteLocationRepository.findBySite(site));
            }
            return allStored;
        }

        @Override
        public Comparator<SiteLocation> getSorter() {
            return new SiteLocationComparator();
        }

        @Override
        public TestEqualityHelper<SiteLocation> getEqualityHelper() {
            TestEqualityHelper.ClassMethodInformation siteMethodInformation =
                    new TestEqualityHelper.ClassMethodInformation(SeadSite.class, "getSiteLocations");
            TestEqualityHelper<SiteLocation> equalityHelper = new TestEqualityHelper<>(true);
            equalityHelper.addMethodInformation(siteMethodInformation);
            return equalityHelper;
        }
    }

    private static class SiteLocationComparator implements Comparator<SiteLocation> {
        @Override
        public int compare(SiteLocation o1, SiteLocation o2) {
            if(o1.getSite().equals(o2.getSite())){
                return o1.getLocation().compareTo(o2.getLocation());
            } else {
                return o1.getSite().compareTo(o2.getSite());
            }
        }
    }
}
