package se.sead.testutils;

import se.sead.bugsimport.tracing.seadmodel.BugsError;
import se.sead.bugsimport.tracing.seadmodel.BugsInformation;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.bugsimport.tracing.seadmodel.BugsTraceType;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AssertHelper {

    private String seadTableName;

    public AssertHelper(String seadTableName){
        this.seadTableName = seadTableName;
    }

    public void assertEmpty(List<? extends BugsInformation> list){
        assertSize(list, 0);
    }

    public void assertSize(List<? extends BugsInformation> list, int size){
        assertEquals(size, list.size());
    }

    public void assertInserts(List<BugsTrace> traces, int numberOfInserts){
        assertNumberOfTracesOfType(traces, numberOfInserts, BugsTraceType.INSERT);
    }

    private void assertNumberOfTracesOfType(List<BugsTrace> traces, int expectedNumberOfTraces, BugsTraceType type){
        assertEquals(
                (long)expectedNumberOfTraces,
                traces.stream()
                .filter(trace -> trace.getType() == type && trace.getSeadTable().equals(seadTableName))
                .count()
        );
    }

    public void assertDeletes(List<BugsTrace> traces, int numberOfDeletes){
        assertNumberOfTracesOfType(traces, numberOfDeletes, BugsTraceType.DELETE);
    }

    public void assertUpdates(List<BugsTrace> traces, int numberOfUpdates){
        assertNumberOfTracesOfType(traces, numberOfUpdates, BugsTraceType.UPDATE);
    }

    public void assertDeleteInsertUpdate(List<BugsTrace> traces, int numberOfUpdates){
        assertNumberOfTracesOfType(traces, numberOfUpdates, BugsTraceType.DELETE);
        assertNumberOfTracesOfType(traces, numberOfUpdates, BugsTraceType.INSERT);
    }

    public void assertContainsError(List<BugsError> errors, String errorMessage){
        assertTrue(
                errors.stream()
                .anyMatch(error -> errorMessage.equals(errorMessage))
        );
    }

    public void assertPrestoredTrace(List<BugsTrace> traces, int... traceIdFromDatabase) {
        assert traceIdFromDatabase != null;
        for (int traceId:
             traceIdFromDatabase) {
                assertPrestoredTrace(traces, traceId);
        }

    }

    private void assertPrestoredTrace(List<BugsTrace> traces, int traceId){
        assertTrue(
                traces.stream()
                        .anyMatch( trace -> trace.getId() == traceId)
        );
    }
}
