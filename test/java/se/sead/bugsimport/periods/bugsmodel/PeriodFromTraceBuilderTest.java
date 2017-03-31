package se.sead.bugsimport.periods.bugsmodel;

import org.junit.Test;
import se.sead.bugsimport.periods.PeriodCreator;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;

import static org.junit.Assert.*;

public class PeriodFromTraceBuilderTest {

    private PeriodFromTraceBuilder traceBuilder;

    @Test
    public void getConvertForTrace(){
        Period test = PeriodCreator.create(
                "CODE",
                "name",
                "type",
                "description",
                "ref",
                "geography",
                0,
                "BC",
                0,
                "AD",
                "yearsType");
        BugsTrace trace = createTrace(test);
        traceBuilder = new PeriodFromTraceBuilder(trace);
        Period periodFromTrace = traceBuilder.createPeriodFromTrace();
        assertEquals(test, periodFromTrace);
    }

    private BugsTrace createTrace(Period period) {
        BugsTrace trace = new BugsTrace();
        trace.setBugsData(period);
        return trace;
    }

    @Test
    public void convertForDataWithComma(){
        Period test = PeriodCreator.create(
                "CODE",
                "name",
                "type",
                "description, with comma",
                "ref",
                "geography",
                0,
                "BC",
                0,
                "AD",
                "yearsType");
        BugsTrace trace = createTrace(test);
        traceBuilder = new PeriodFromTraceBuilder(trace);
        Period periodFromTrace = traceBuilder.createPeriodFromTrace();
        assertEquals(test, periodFromTrace);
    }
}