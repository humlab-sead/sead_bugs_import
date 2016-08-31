package se.sead.siteotherproxies;

import se.sead.utils.BigDecimalDefinition;
import se.sead.bugsimport.site.seadmodel.SeadSite;
import se.sead.bugsimport.siteotherproxies.bugsmodel.SiteOtherProxies;
import se.sead.bugsimport.siteotherproxies.seadmodel.SiteOtherRecord;
import se.sead.bugsimport.tracing.seadmodel.BugsError;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.bugsimport.tracing.seadmodel.BugsTraceType;
import se.sead.model.TestSeadSite;
import se.sead.model.TestSiteOtherRecord;
import se.sead.repositories.RecordTypeRepository;
import se.sead.repositories.SiteRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ImportTestDefinition {

    private RecordTypeRepository recordTypeRepository;

    private SeadSite site00006Stored;
    private SeadSite site00010Updated;
    private SeadSite site00008AddedSite;

    ImportTestDefinition(RecordTypeRepository recordTypeRepository, SiteRepository siteRepository){
        this.recordTypeRepository = recordTypeRepository;
        setupSites(siteRepository);
    }

    private void setupSites(SiteRepository siteRepository){
        site00006Stored = siteRepository.findOne(2);
        site00010Updated = siteRepository.findOne(1);
        site00008AddedSite = TestSeadSite.create(
                null,
                "Agerod V",
                null,
                BigDecimalDefinition.convertToSeadContext(55.934105f),
                BigDecimalDefinition.convertToSeadContext(13.400202f),
                BigDecimalDefinition.convertToSeadContext(39f),
                "Ageröd V. Specimens hand picked by the excavators of a mesolithic midden.");
    }

    public List<SiteOtherRecord> getExpectedImportedResults(){
        List<SiteOtherRecord> expectedItems = new ArrayList<>();
        expectedItems.addAll(createPreStored());
        expectedItems.addAll(createNewItems());
        return expectedItems;
    }

    private List<SiteOtherRecord> createPreStored() {
        List<SiteOtherRecord> prestored = new ArrayList<>();
        // site 00006 items
        prestored.add(TestSiteOtherRecord.create(5, recordTypeRepository.findOne(18), site00006Stored, null));
        prestored.add(TestSiteOtherRecord.create(6, recordTypeRepository.findOne(16), site00006Stored, null));

        // site 00010 unchanged items
        prestored.add(TestSiteOtherRecord.create(1, recordTypeRepository.findOne(18), site00010Updated, null));
        prestored.add(TestSiteOtherRecord.create(2, recordTypeRepository.findOne(14), site00010Updated, null));
        prestored.add(TestSiteOtherRecord.create(3, recordTypeRepository.findOne(16), site00010Updated, null));
        prestored.add(TestSiteOtherRecord.create(4, recordTypeRepository.findOne(4), site00010Updated, null));

        return prestored;
    }

    private List<SiteOtherRecord> createNewItems(){
        List<SiteOtherRecord> created = new ArrayList<>();

        //site 0008 new site
        created.add(TestSiteOtherRecord.create(null, recordTypeRepository.findOne(16), site00008AddedSite, null));

        // added item for site00010
        created.add(TestSiteOtherRecord.create(null, recordTypeRepository.findOne(17), site00010Updated, null));
        return created;
    }

    public void logCheck(SiteOtherProxies bugsData, List<BugsTrace> traces, List<BugsError> errors) {
        switch(bugsData.getSiteCode()){
            case "SITE000006":
                assertTrue(traces.isEmpty());
                assertTrue(errors.isEmpty());
                break;
            case "SITE000008":
                assertEquals(BugsTraceType.INSERT, traces.get(0).getType());
                assertTrue(errors.isEmpty());
                break;
            case "SITE000009":
                assertTrue(traces.isEmpty());
                assertEquals("Site is not imported, ignoring", errors.get(0).getMessage());
                break;
            case "SITE000010":
                assertTrue(errors.isEmpty());
                assertEquals(2, traces.size());
                List<BugsTraceType> traceTypes = traces.stream().map(trace -> trace.getType()).collect(Collectors.toList());
                assertTrue(traceTypes.contains(BugsTraceType.INSERT) && traceTypes.contains(BugsTraceType.DELETE));
                break;
        }
    }
}
