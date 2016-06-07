package se.sead.mcr;

import se.sead.bugsimport.mcr.bugsmodel.MCRSummaryData;
import se.sead.bugsimport.mcr.seadmodel.MCRSummary;
import se.sead.bugsimport.tracing.seadmodel.BugsError;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.model.TestMCRSummary;
import se.sead.util.TestSpeciesProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MCRSummarTestDefinition {

    static List<MCRSummaryData> EXPECTED_BUGS_DATA =
            Arrays.asList(
                create(01.0010020d,14,29,-29,7,12,49,22,30),
                create(01.0010070d,15,28,-26,13,11,41,22,26),
                create(01.0020010d,14,15,-10,1,12,34,22,23)
            );

    private static MCRSummaryData create(
            Double code,
            Integer maxLo,
            Integer maxHi,
            Integer minLo,
            Integer minHi,
            Integer rangeLo,
            Integer rangeHi,
            Integer cogMidMax,
            Integer cogMidRange){
        MCRSummaryData summary = new MCRSummaryData();
        summary.setCode(code);
        summary.setMaxLo(maxLo.shortValue());
        summary.setMaxHi(maxHi.shortValue());
        summary.setMinLo(minLo.shortValue());
        summary.setMinHi(minHi.shortValue());
        summary.setRangeLo(rangeLo.shortValue());
        summary.setRangeHi(rangeHi.shortValue());
        summary.setCogMidTMax(cogMidMax.shortValue());
        summary.setCogMidTRange(cogMidRange.shortValue());
        return summary;
    }

    private TestSpeciesProvider speciesMaterial;

    MCRSummarTestDefinition(TestSpeciesProvider speciesProvider){
        speciesMaterial = speciesProvider;
    }

    List<MCRSummary> getExpectedResults(){
        List<MCRSummary> expected = new ArrayList<>();
        expected.add(TestMCRSummary.create(1, speciesMaterial.getSpecies("01.0010020"), 14, 29, -29, 7, 12, 49, 22,30));
        expected.add(TestMCRSummary.create(null, speciesMaterial.getSpecies("01.0010070"), 15,28,-26,13,11,41,22,26));
        return expected;
    }

    void assertTraces(List<BugsTrace> traces, MCRSummaryData bugsData){
        if(bugsData.getCode() == 01.0010020d ||
                bugsData.getCode() == 01.0020010d){
            assertEquals(0, traces.size());
        } else if(bugsData.getCode() == 01.0010070d){
            assertEquals(1, traces.size());
            BugsTrace bugsTrace = traces.get(0);
            assertEquals("tbl_mcr_summary_data", bugsTrace.getSeadTable());
        }
    }

    void assertErrors(List<BugsError> errors, MCRSummaryData bugsData){
        if(bugsData.getCode() == 01.0020010d){
            assertEquals(1, errors.size());
            BugsError bugsError = errors.get(0);
            assertEquals("No species found for code: " + bugsData.getCode(), bugsError.getMessage());
        } else {
            assertEquals(0, errors.size());
        }
    }

}
