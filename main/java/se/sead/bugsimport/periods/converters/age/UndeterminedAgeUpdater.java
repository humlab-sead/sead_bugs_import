package se.sead.bugsimport.periods.converters.age;

import se.sead.bugsimport.periods.bugsmodel.Period;
import se.sead.bugsimport.periods.seadmodel.RelativeAge;

import java.math.BigDecimal;
import java.util.Arrays;

public class UndeterminedAgeUpdater extends AgeUpdater {

    protected UndeterminedAgeUpdater(RelativeAge original, Period bugsData) {
        super(new NullConverter(), original, bugsData);
    }

    @Override
    protected BigDecimal getOriginalBeginDate() {
        return null;
    }

    @Override
    protected void setBeginDate(BigDecimal convertedDate) {
        original.addError("No type set for period");
    }

    @Override
    protected BigDecimal getOriginalEndDate() {
        return null;
    }

    @Override
    protected void setEndDate(BigDecimal convertedDate) {

    }

    private static class NullConverter extends AgeConverter {

        protected NullConverter() {
            super(Arrays.asList(new PassthroughConverter()));
        }
    }

    private static class PassthroughConverter implements AgeConversion{

        @Override
        public boolean handleValue(String bcad, Integer bugsData) {
            return true;
        }

        @Override
        public BigDecimal convert(String bcad, Integer bugsData) {
            if(bugsData != null){
                return BigDecimal.valueOf(bugsData);
            }
            return null;
        }
    }
}
