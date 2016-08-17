package se.sead.util;

import org.junit.Assert;
import org.junit.Test;
import se.sead.testutils.BigDecimalDefinition;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestBigDecimalDefinition {

    @Test
    public void nullsAreEqual(){
        assertTrue(BigDecimalDefinition.equalBigDecimalNumericValues(null, null));
    }

    @Test
    public void oneNullisNotEqual(){
        assertFalse(BigDecimalDefinition.equalBigDecimalNumericValues(BigDecimal.ONE, null));
        assertFalse(BigDecimalDefinition.equalBigDecimalNumericValues(null, BigDecimal.ONE));
    }

    @Test
    public void sameObjectIsEqual(){
        assertTrue(BigDecimalDefinition.equalBigDecimalNumericValues(BigDecimal.ONE, BigDecimal.ONE));
    }

    @Test
    public void differentContextAreStillEqual(){
        BigDecimal first = new BigDecimal("1.6789", new MathContext(10, RoundingMode.FLOOR));
        BigDecimal second = new BigDecimal("1.6789", new MathContext(12, RoundingMode.FLOOR));
        assertTrue(BigDecimalDefinition.equalBigDecimalNumericValues(first, second));
    }

    @Test
    public void differentScalesPointsAreStillEqual(){
        BigDecimal first = new BigDecimal("01.1110000", new MathContext(18, RoundingMode.FLOOR));
        first.setScale(10);
        BigDecimal second = new BigDecimal("01.111");
        assertTrue(BigDecimalDefinition.equalBigDecimalNumericValues(first,second));
    }


}
