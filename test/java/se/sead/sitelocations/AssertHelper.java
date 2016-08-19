package se.sead.sitelocations;

import se.sead.bugsimport.tracing.seadmodel.BugsError;
import se.sead.bugsimport.tracing.seadmodel.BugsInformation;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.bugsimport.tracing.seadmodel.BugsTraceType;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

class AssertHelper {

    static void assertEmpty(List<? extends BugsInformation> list){
        assertSize(list, 0);
    }

    static void assertSize(List<? extends BugsInformation> list, int size){
        assertEquals(size, list.size());
    }

    static void assertLocationInserts(List<BugsTrace> traces, int numberOfInserts){
        assertNumberOfTracesOfTypeWithTable(traces, numberOfInserts, BugsTraceType.INSERT, "tbl_locations");
    }

    static void assertInserts(List<BugsTrace> traces, int numberOfInserts){
        assertNumberOfTracesOfType(traces, numberOfInserts, BugsTraceType.INSERT);
    }

    static void assertNumberOfTracesOfType(List<BugsTrace> traces, int expectedNumberOfTraces, BugsTraceType type){
        assertNumberOfTracesOfTypeWithTable(traces, expectedNumberOfTraces, type, "tbl_site_locations");
    }

    private static void assertNumberOfTracesOfTypeWithTable(List<BugsTrace> traces, int expectedNumberOfTraces, BugsTraceType type, String seadTableName){
        assertEquals(
                (long)expectedNumberOfTraces,
                traces.stream()
                .filter(trace -> trace.getType() == type && trace.getSeadTable().equals(seadTableName))
                .count()
        );
    }

    static void assertUpdates(List<BugsTrace> traces, int numberOfUpdates){
        assertNumberOfTracesOfType(traces, numberOfUpdates, BugsTraceType.DELETE);
        assertNumberOfTracesOfType(traces, numberOfUpdates, BugsTraceType.INSERT);
    }

    static void assertContainsError(List<BugsError> errors, String errorMessage){
        assertTrue(
                errors.stream()
                .anyMatch(error -> errorMessage.equals(errorMessage))
        );
    }

}
