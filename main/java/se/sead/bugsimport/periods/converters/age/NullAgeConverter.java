package se.sead.bugsimport.periods.converters.age;

import java.math.BigDecimal;

class NullAgeConverter implements AgeConversion {
    @Override
    public boolean handleValue(String bcad, Integer bugsData) {
        return bugsData == null;
    }

    @Override
    public BigDecimal convert(String bcad, Integer bugsData) {
        return null;
    }
}
