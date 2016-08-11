package se.sead.sitereferences;

import se.sead.BigDecimalDefinition;
import se.sead.bugsimport.site.seadmodel.SeadSite;
import se.sead.bugsimport.sitereferences.bugsmodel.BugsSiteRef;
import se.sead.bugsimport.sitereferences.seadmodel.SiteReference;
import se.sead.bugsimport.tracing.seadmodel.BugsError;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.bugsimport.tracing.seadmodel.BugsTraceType;
import se.sead.model.TestSeadSite;
import se.sead.model.TestSiteReference;
import se.sead.repositories.BiblioDataRepository;
import se.sead.repositories.SiteRepository;
import se.sead.sead.model.Biblio;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ImportTestDefinition {

    private BiblioDataRepository biblioRepository;

    private SeadSite site000006;
    private SeadSite site000008;
    private SeadSite site000009;
    private SeadSite site000010;
    private SeadSite site000012;

    ImportTestDefinition(SiteRepository siteRepository, BiblioDataRepository biblioRepository){
        this.biblioRepository = biblioRepository;
        setupSites(siteRepository);
    }

    private void setupSites(SiteRepository siteRepository){
        site000006 = siteRepository.findOne(1);
        site000008 = siteRepository.findOne(2);
        site000010 = siteRepository.findOne(3);
        site000012 = siteRepository.findOne(4);
        site000009 = TestSeadSite.create(
                null,
                "Aghnadarragh",
                "NG 457642",
                BigDecimalDefinition.convertToSeadContext(54.596592f),
                BigDecimalDefinition.convertToSeadContext(-6.255964f),
                BigDecimalDefinition.convertToSeadContext(30f),
                "Woody detritus peat overlying organic silt between Early Midlandian and Late Midlandian tills. [Dates need checking - duplicate/missing lab numbers, SSR should be SRR?]\r\nU8  Detrital mud.\r\nU6  Woody detrital peat."
        );
    }

    List<SiteReference> getExpectedData(){
        List<SiteReference> siteReferences = new ArrayList<>();
        siteReferences.add(TestSiteReference.create(null, site000006, getReference("robinson (1979b)")));
        siteReferences.add(TestSiteReference.create(2, site000008, getReference("lemdahl (1983)")));
        siteReferences.add(TestSiteReference.create(1, site000008, getReference("lemdahl & nilsson (1982)")));
        siteReferences.add(TestSiteReference.create(null, site000009, getReference("mccabe et al. (1987)")));
        siteReferences.add(TestSiteReference.create(3, site000010, getReference("Panagiotakopulu & Buckland (1991)")));
        siteReferences.add(TestSiteReference.create(4, site000010, getReference("Panagiotakopulu et al. (1997)")));
        siteReferences.add(TestSiteReference.create(null, site000010, getReference("Panagiotakopulu (2000)")));
        siteReferences.add(TestSiteReference.create(null, site000010, getReference("lemdahl (1983)")));
        return siteReferences;
    }

    private Biblio getReference(String bugsReference) {
        Biblio ref = biblioRepository.getByBugsReferenceIgnoreCase(bugsReference);
        if(ref == null){
            throw new NullPointerException(bugsReference);
        }
        return ref;
    }

    public void verifyTraceInformation(String bugsSite, List<BugsTrace> traces, List<BugsError> errors) {
        switch(bugsSite){
            case "SITE000006":
            case "SITE000009":
                assertEquals(1, traces.size());
                assertEquals(BugsTraceType.INSERT, traces.get(0).getType());
                assertTrue(errors.isEmpty());
                break;
            case "SITE000008":
                assertTrue(traces.isEmpty());
                assertTrue(errors.isEmpty());
                break;
            case "SITE000010":
                assertEquals(2, traces.size());
                assertTrue(traces.stream().allMatch(trace -> trace.getType() == BugsTraceType.INSERT));
                assertTrue(errors.isEmpty());
                break;
            case "SITE000011":
                assertTrue(traces.isEmpty());
                assertEquals(1,errors.size());
                assertEquals("Missing site: SITE000011", errors.get(0).getMessage());
                break;
            case "SITE000012":
                assertTrue(traces.isEmpty());
                assertEquals(1, errors.size());
                assertEquals("Missing reference: Girling (1986a)", errors.get(0).getMessage());
                break;
        }
    }
}
