package se.sead.countsheets;

import se.sead.BigDecimalDefinition;
import se.sead.bugsimport.countsheets.bugsmodel.Countsheet;
import se.sead.bugsimport.countsheets.seadmodel.SampleGroup;
import se.sead.bugsimport.site.seadmodel.SeadSite;
import se.sead.bugsimport.tracing.seadmodel.BugsError;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.bugsimport.tracing.seadmodel.BugsTraceType;
import se.sead.model.TestSampleGroup;
import se.sead.model.TestSeadSite;
import se.sead.repositories.MethodRepository;
import se.sead.repositories.SamplingContextRepository;
import se.sead.repositories.SiteRepository;
import se.sead.sead.methods.Method;
import se.sead.sead.model.SamplingContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ImportTestDefinition {

    private SamplingContextRepository contextRepository;
    private MethodRepository methodRepository;

    private SeadSite site6;
    private SeadSite site7;
    private SeadSite site9;
    private SeadSite site10;
    private SeadSite site158;
    private SeadSite site251;
    private SeadSite site969;

    ImportTestDefinition(
            SamplingContextRepository contextRepository,
            SiteRepository siteRepository,
            MethodRepository methodRepository){
        this.contextRepository = contextRepository;
        this.methodRepository = methodRepository;
        setupSites(siteRepository);
    }

    private void setupSites(SiteRepository siteRepository){
        site6 = siteRepository.findOne(1);
        site9 = siteRepository.findOne(2);
        site10 = siteRepository.findOne(3);
        site158 = siteRepository.findOne(4);
        site251 = siteRepository.findOne(5);
        site969 = siteRepository.findOne(6);
        site7 = TestSeadSite.create(
                null,
                "Abydos mummies",
                null,
                BigDecimalDefinition.convertToSeadContext(26.145428f),
                BigDecimalDefinition.convertToSeadContext(31.966911f),
                BigDecimalDefinition.convertToSeadContext(86f),
                "Old Kingdom mummies.  Lice from hair and wigs.");
    }

    List<SampleGroup> getExpectedSeadData(){
        List<SampleGroup> expectedSampleGroups = new ArrayList<>();
        SamplingContext archSiteContext = contextRepository.findByNameIgnoreCase("Archaeological site");
        SamplingContext stratigraphicSeq = contextRepository.findByNameIgnoreCase("Stratigraphic sequence");
        SamplingContext otherPaleo = contextRepository.findByNameIgnoreCase("Other palaeo");
        SamplingContext pitfallTraps = contextRepository.findByNameIgnoreCase("Pitfall traps");
        SamplingContext otherModern = contextRepository.findByNameIgnoreCase("Other modern");
        Method tempMethod = methodRepository.findOne(1);
        expectedSampleGroups.add(
                TestSampleGroup.create(1, "Abingdon Stert Street_bugsdata", null, site6, archSiteContext, tempMethod)
        );
        expectedSampleGroups.add(
                TestSampleGroup.create(null, "Abydos_PA_bugsdata.xls", null, site7, archSiteContext, tempMethod)
        );
        expectedSampleGroups.add(
                TestSampleGroup.create(null, "Aghnadarragh_bugsdata.xls", null, site9, stratigraphicSeq, tempMethod)
        );
        expectedSampleGroups.add(
                TestSampleGroup.create(null, "Akrotiri_PA_bugsdata.XLS", null, site10, archSiteContext, tempMethod)
        );
        expectedSampleGroups.add(
                TestSampleGroup.create(2, "Akrotiri Silk", null, site10, archSiteContext, tempMethod)
        );
        expectedSampleGroups.add(
                TestSampleGroup.create(null, "West House", null, site10, archSiteContext, tempMethod)
        );
        expectedSampleGroups.add(
                TestSampleGroup.create(null, "West House Samples", null, site10, archSiteContext, tempMethod)
        );
        expectedSampleGroups.add(
                TestSampleGroup.create(null, "House of the Ladies", null, site10, archSiteContext, tempMethod)
        );
        expectedSampleGroups.add(
                TestSampleGroup.create(null, "Dendermonde", null, site158, otherPaleo, tempMethod)
        );
        expectedSampleGroups.add(
                TestSampleGroup.create(3, "Garden Under Sandet 1995_bugsdata.XLS", null, site251, archSiteContext, tempMethod)
        );
        expectedSampleGroups.add(
                TestSampleGroup.create(null, "GUS modern converted bugsdata.xls", null, site251, pitfallTraps, tempMethod)
        );
        expectedSampleGroups.add(
                TestSampleGroup.create(null, "GUS Full List Bugs Format.xls", null, site251, archSiteContext, tempMethod)
        );
        expectedSampleGroups.add(
                TestSampleGroup.create(null, "Knepp cattle Dung 09", null, site969, otherModern, tempMethod)
        );
        expectedSampleGroups.add(
                TestSampleGroup.create(4, "New name", null, site6, stratigraphicSeq, tempMethod)
        );
        expectedSampleGroups.add(
                TestSampleGroup.create(5, "Name exists outside of bugs", null, site10, archSiteContext, tempMethod)
        );
        return expectedSampleGroups;
    }

    void checkTracesAndErrors(Countsheet bugsData, List<BugsTrace> traces, List<BugsError> errors){
        switch(bugsData.getCode()){
            case "COUN000482":
            case "COUN000927":
            case "COUN000132":
                assertTrue(traces.isEmpty());
                assertTrue(errors.isEmpty());
                break;
            case "COUN000384":
            case "COUN000030":
            case "COUN000006":
            case "COUN001334":
            case "COUN001335":
            case "COUN001336":
            case "COUN000795":
            case "COUN000760":
            case "COUN000820":
            case "COUN000925":
                assertEquals(1, traces.size());
                assertEquals(BugsTraceType.INSERT, traces.get(0).getType());
                assertTrue(errors.isEmpty());
                break;
            case "COUN001337":
                assertOneError(traces, errors, "Empty context not allowed");
                break;
            case "COUN001338":
                assertOneError(traces, errors, "Unknown context: Unknown context");
                break;
            case "COUN000926":
                assertOneError(traces, errors, "No site found: SITE000970");
                break;
            case "COUNNONAME":
                assertOneError(traces, errors, "Empty name not allowed");
                break;
            case "COUNUPDATE":
                assertEquals(1, traces.size());
                assertEquals(BugsTraceType.UPDATE, traces.get(0).getType());
                assertTrue(errors.isEmpty());
                break;
            case "COUNBYNAME":
                assertOneError(
                        traces,
                        errors,
                        "Name found for site, but no bugs import marker: change name? countsheet=" + bugsData.getName() + "site=" + bugsData.getSiteCode());
        }
    }

    private void assertOneError(List<BugsTrace> traces, List<BugsError> errors, String expectedErrorMessage) {
        assertTrue(traces.isEmpty());
        assertEquals(1, errors.size());
        assertEquals(expectedErrorMessage, errors.get(0).getMessage());
    }
}
