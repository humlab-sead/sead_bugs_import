package se.sead.periods.bugsmodel;

import org.junit.Test;
import se.sead.bugsimport.periods.bugsmodel.Period;
import se.sead.bugsimport.periods.bugsmodel.PeriodFromTraceBuilder;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;

import static org.junit.Assert.assertEquals;

public class PeriodFromTraceBuilderTest {

    private static final String PERIOD_DEFINITION_RADIO =
            "{Anglian,Anglian,Geological,=MIS-12;Cold,Bowen 1999,UK,478000,BP,423000,BP,Radiometric}";

    @Test(expected = NullPointerException.class)
    public void noCompressedStringResultsInError(){
        PeriodFromTraceBuilder builder = new PeriodFromTraceBuilder(new BugsTrace());
    }

    @Test
    public void compressToString(){
        Period period = new Period();
        period.setPeriodCode("Anglian");
        period.setName("Anglian");
        period.setType("Geological");
        period.setDesc("=MIS-12;Cold");
        period.setRef("Bowen 1999");
        period.setGeography("UK");
        period.setBegin(478000);
        period.setBeginBCad("BP");
        period.setEnd(423000);
        period.setEndBCad("BP");
        period.setYearsType("Radiometric");
        String compressedString = period.compressToString();
        assertEquals(PERIOD_DEFINITION_RADIO, compressedString);
    }

    @Test
    public void radioPeriodFromTrace(){
        Period period = new Period();
        period.setPeriodCode("Anglian");
        period.setName("Anglian");
        period.setType("Geological");
        period.setDesc("=MIS-12;Cold");
        period.setRef("Bowen 1999");
        period.setGeography("UK");
        period.setBegin(478000);
        period.setBeginBCad("BP");
        period.setEnd(423000);
        period.setEndBCad("BP");
        period.setYearsType("Radiometric");
        BugsTrace trace = new BugsTrace();
        trace.setBugsData(period);
        PeriodFromTraceBuilder builder = new PeriodFromTraceBuilder(trace);
        Period periodFromTrace = builder.createPeriodFromTrace();
        assertEquals(period, periodFromTrace);
    }

    @Test
    public void radionPeriodFromTraceWithNullStart(){
        Period period = new Period();
        period.setPeriodCode("Anglian");
        period.setName("Anglian");
        period.setType("Geological");
        period.setDesc("=MIS-12;Cold");
        period.setRef("Bowen 1999");
        period.setGeography("UK");
        period.setBegin(null);
        period.setBeginBCad("BP");
        period.setEnd(423000);
        period.setEndBCad("BP");
        period.setYearsType("Radiometric");
        BugsTrace trace = new BugsTrace();
        trace.setBugsData(period);
        PeriodFromTraceBuilder builder = new PeriodFromTraceBuilder(trace);
        Period periodFromTrace = builder.createPeriodFromTrace();
        assertEquals(period, periodFromTrace);
    }
}
