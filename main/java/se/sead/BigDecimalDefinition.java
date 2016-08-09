package se.sead;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class BigDecimalDefinition {

    public static final MathContext SEAD_MATH_CONTEXT = new MathContext(18, RoundingMode.HALF_DOWN);
    public static final int SEAD_SCALE = 10;

    public static final BigDecimal convertToSeadContext(Float bugsValue){
        if(bugsValue == null){
            return null;
        } else {
            BigDecimal value = new BigDecimal(Float.toString(bugsValue), SEAD_MATH_CONTEXT);
            value.setScale(SEAD_SCALE);
            return value;
        }
    }

    public static final boolean equalBigDecimalNumericValues(BigDecimal first, BigDecimal second){
        if(first == null && second == null){
            return true;
        }
        if(first == null && second != null ||
                first != null && second == null){
            return false;
        }
        return first.compareTo(second) == 0;
    }
}
