package se.sead.sitereferences;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import se.sead.bugsimport.site.seadmodel.SeadSite;
import se.sead.bugsimport.sitereferences.SiteReferencesImporter;
import se.sead.bugsimport.sitereferences.bugsmodel.BugsSiteRef;
import se.sead.bugsimport.sitereferences.seadmodel.SiteReference;
import se.sead.bugsimport.tracing.seadmodel.BugsError;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.model.TestEqualityHelper;
import se.sead.repositories.*;
import se.sead.testutils.DefaultConfig;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
public class SiteReferencesImportTest {

    @TestConfiguration
    public static class Config extends DefaultConfig {
        public Config(){
            super("sitereferences");
        }
    }

    @Autowired
    private SiteRepository siteRepository;
    @Autowired
    private BiblioDataRepository biblioRepository;
    @Autowired
    private BugsTraceRepository traceRepository;
    @Autowired
    private BugsErrorRepository errorRepository;
    @Autowired
    private SiteReferenceRepository siteReferenceRepository;
    @Autowired
    private SiteReferencesImporter importer;

    private ImportTestDefinition testDefinition;

    @Test
    public void run(){
        testDefinition = new ImportTestDefinition(siteRepository, biblioRepository);
        importer.run();
        verifyDatabaseContent();
        verifyTraces();
    }

    private TestEqualityHelper.ClassMethodInformation siteEqualityInformation =
            new TestEqualityHelper.ClassMethodInformation(SeadSite.class, "getSiteLocations");

    private void verifyDatabaseContent() {
        List<SiteReference> expectedData = testDefinition.getExpectedData();
        List<SiteReference> actualData = getActualData();
        assertEquals(expectedData.size(), actualData.size());
        SiteReferencesComparator comparator = new SiteReferencesComparator();
        Collections.sort(expectedData, comparator);
        Collections.sort(actualData, comparator);
        TestEqualityHelper<SiteReference> equalityHelper = new TestEqualityHelper<>(true);
        equalityHelper.addMethodInformation(siteEqualityInformation);
        for (int i = 0; i < expectedData.size(); i++) {
            SiteReference expected = expectedData.get(i);
            SiteReference actual = actualData.get(i);
            try {
                if (expected.getId() == null) {
                    assertTrue(equalityHelper.equalsWithoutBlackListedMethods(expected, actual));
                } else {
                    assertEquals(expected, actual);
                }
            } catch (AssertionError ae){
                System.out.println(expected);
                System.out.println(actual);
                throw ae;
            }
        }
    }

    private List<SiteReference> getActualData() {
        List<SeadSite> allSites = siteRepository.findAllByNameStartingWithIgnoreCase("");
        List<SiteReference> actualReferences = new ArrayList<>();
        for (SeadSite site :
                allSites) {
            actualReferences.addAll(siteReferenceRepository.findAllBySite(site));
        }
        return actualReferences;
    }

    private void verifyTraces() {
        LogDataManager tracesAndErrors = new LogDataManager();
        for (BugsSiteRef bugsData :
                ExpectedBugsSiteRefData.EXPECTED_BUGS_DATA) {
            List<BugsTrace> traces = traceRepository.findByBugsTableAndCompressedBugsData("TSiteRef", bugsData.compressToString());
            List<BugsError> errors = errorRepository.findByBugsTableAndCompressedBugsData("TSiteRef", bugsData.compressToString());
            tracesAndErrors.addTraces(bugsData.getSiteCode(), traces);
            tracesAndErrors.addErrors(bugsData.getSiteCode(), errors);
        }
        for (String siteCode :
                tracesAndErrors.getBugsSiteCodes()) {
            testDefinition.verifyTraceInformation(
                    siteCode,
                    tracesAndErrors.getTracesForSite(siteCode),
                    tracesAndErrors.getErrorsForSite(siteCode)
            );
        }
    }

    private static class SiteReferencesComparator implements Comparator<SiteReference>{

        @Override
        public int compare(SiteReference o1, SiteReference o2) {
            String o1Data = o1.getSite().getName() + o1.getReference().getBugsReference();
            String o2Data = o1.getSite().getName() + o2.getReference().getBugsReference();
            return o1Data.compareTo(o2Data);
        }
    }

    private static class LogDataManager {
        private List<String> bugsSiteCodes;
        private Map<String, List<BugsTrace>> traces;
        private Map<String, List<BugsError>> errors;

        LogDataManager(){
            bugsSiteCodes = new ArrayList<>();
            traces = new HashMap<>();
            errors = new HashMap<>();
        }

        void addTraces(String bugsSiteCode, List<BugsTrace> trace){
            List<BugsTrace> siteTraces = traces.get(bugsSiteCode);
            if(siteTraces == null){
                siteTraces = new ArrayList<>();
                traces.put(bugsSiteCode, siteTraces);
            }
            siteTraces.addAll(trace);
            addSiteCode(bugsSiteCode);
        }

        private void addSiteCode(String siteCode){
            if(!bugsSiteCodes.contains(siteCode)){
                bugsSiteCodes.add(siteCode);
            }
        }

        void addErrors(String bugsSiteCode, List<BugsError> error){
            List<BugsError> siteErrors = errors.get(bugsSiteCode);
            if(siteErrors == null){
                siteErrors = new ArrayList<>();
                errors.put(bugsSiteCode, siteErrors);
            }
            siteErrors.addAll(error);
            addSiteCode(bugsSiteCode);
        }

        List<String> getBugsSiteCodes(){ return bugsSiteCodes;}

        List<BugsTrace> getTracesForSite(String siteCode){
            return traces.get(siteCode);
        }

        List<BugsError> getErrorsForSite(String siteCode){
            return errors.get(siteCode);
        }
    }

}
