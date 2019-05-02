package se.sead.periods.converters.age;

import org.junit.Before;
import org.junit.Test;
import se.sead.bugsimport.periods.bugsmodel.Period;
import se.sead.bugsimport.periods.converters.age.CalendarAgeConverter;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CalendarAgeConverterTest {

    private static final MathContext SEAD_AGE_CONTEXT = new MathContext(20, RoundingMode.HALF_DOWN);
    private static final int AGE_SCALE = 5;

    private CalendarAgeConverter converter;

    @Before
    public void setup(){
        converter = new CalendarAgeConverter();
    }

    @Test
    public void beginNullDateValueIsNullResult(){
        Period data = new Period();
        data.setBegin(null);
        assertNull(converter.getBeginAge(data));
    }

    @Test
    public void endNullDateValueIsNullResult(){
        Period data = new Period();
        data.setEnd(null);
        assertNull(converter.getEndAge(data));
    }

    @Test
    public void beginBCADisBPResultsInSameValue(){
        Period data = new Period();
        data.setBegin(1000);
        data.setBeginBCad("BP");
        assertEquals(createExpectedValue(1000), converter.getBeginAge(data));
    }

    private BigDecimal createExpectedValue(int value) {
        BigDecimal expected = new BigDecimal(value, SEAD_AGE_CONTEXT);
        expected.setScale(AGE_SCALE);
        return expected;
    }

    @Test
    public void endBCADisBPResultsInNull(){
        Period data = new Period();
        data.setEnd(1000);
        data.setEndBCad("BP");
        assertEquals(createExpectedValue(1000), converter.getEndAge(data));
    }

    @Test
    public void beginBCDate(){
        Period data = new Period();
        data.setBegin(1000);
        data.setBeginBCad("BC");
        BigDecimal expected = createExpectedValue(2950);
        assertEquals(expected, converter.getBeginAge(data));
    }

    @Test
    public void endBCDate(){
        Period data = new Period();
        data.setEnd(1000);
        data.setEndBCad("BC");
        BigDecimal expected = createExpectedValue(2950);
        assertEquals(expected, converter.getEndAge(data));
    }

    @Test
    public void beginADDate(){
        Period data = new Period();
        data.setBegin(1000);
        data.setBeginBCad("AD");
        BigDecimal expected = createExpectedValue((1950 - 1000));
        assertEquals(expected, converter.getBeginAge(data));
    }

    @Test
    public void endADDate(){
        Period data = new Period();
        data.setEnd(1000);
        data.setEndBCad("AD");
        BigDecimal expected = createExpectedValue((1950 - 1000));
        assertEquals(expected, converter.getEndAge(data));
    }

}
