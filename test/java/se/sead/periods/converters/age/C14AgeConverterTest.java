package se.sead.periods.converters.age;

import org.junit.Before;
import org.junit.Test;
import se.sead.bugsimport.periods.bugsmodel.Period;
import se.sead.bugsimport.periods.converters.age.C14AgeConverter;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class C14AgeConverterTest {

    private C14AgeConverter converter;

    @Before
    public void setup(){
        converter = new C14AgeConverter();
    }

    @Test
    public void beginDateIsNullResultsInNull(){
        Period data = new Period();
        data.setBegin(null);
        BigDecimal beginAge = converter.getBeginAge(data);
        assertNull(beginAge);
    }

    @Test
    public void endDateIsNullResultIsNull(){
        Period data = new Period();
        data.setEnd(null);
        BigDecimal endAge = converter.getEndAge(data);
        assertNull(endAge);
    }

    @Test
    public void beginBcadIsNotBPResultInNull(){
        Period data = new Period();
        data.setBegin(1000);
        data.setBeginBCad("AD");
        BigDecimal beginAge = converter.getBeginAge(data);
        assertNull(beginAge);
    }

    @Test
    public void endBcadIsADResultInNull(){
        Period data = new Period();
        data.setEnd(1000);
        data.setEndBCad("AD");
        BigDecimal endAge = converter.getEndAge(data);
        assertNull(endAge);
    }

    @Test
    public void beginBcadValueNullAndDate0ResultIn0(){
        Period data = new Period();
        data.setBegin(0);
        data.setBeginBCad(null);
        BigDecimal beginAge = converter.getBeginAge(data);
        assertTrue(BigDecimal.ZERO.compareTo(beginAge) == 0);
    }

    @Test
    public void endBcadValueNullAndDate0ResultIn0(){
        Period data = new Period();
        data.setEnd(0);
        data.setEndBCad(null);
        BigDecimal endAge = converter.getEndAge(data);
        assertTrue(BigDecimal.ZERO.compareTo(endAge) == 0);
    }

    @Test
    public void beginValuePastThroughOnBPType(){
        MathContext seadAgeContext = new MathContext(20, RoundingMode.HALF_DOWN);
        Period data = new Period();
        data.setBegin(1000);
        data.setBeginBCad("BP");
        BigDecimal expected = new BigDecimal(1000, seadAgeContext);
        expected.setScale(5);
        assertEquals(expected, converter.getBeginAge(data));
    }

    @Test
    public void endValuePastThroughOnBPType(){
        MathContext seadAgeContext = new MathContext(20, RoundingMode.HALF_DOWN);
        Period data = new Period();
        data.setEnd(1000);
        data.setEndBCad("BP");
        BigDecimal expected = new BigDecimal(1000, seadAgeContext);
        expected.setScale(5);
        assertEquals(expected, converter.getEndAge(data));
    }

}
