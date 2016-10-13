package se.sead.bugsimport.periods.converters.age;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class AgeBigDecimalHelper {

    private static final MathContext SEAD_AGE_MATH_CONTEXT = new MathContext(20, RoundingMode.HALF_DOWN);
    private static final int SEAD_AGE_SCALE = 5;

    static BigDecimal createAge(Integer value){
        BigDecimal age = new BigDecimal(value, SEAD_AGE_MATH_CONTEXT);
        age.setScale(SEAD_AGE_SCALE);
        return age;
    }
}
