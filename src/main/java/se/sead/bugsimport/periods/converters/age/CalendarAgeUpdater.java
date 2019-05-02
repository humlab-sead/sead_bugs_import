package se.sead.bugsimport.periods.converters.age;

import se.sead.bugsimport.periods.bugsmodel.Period;
import se.sead.bugsimport.periods.seadmodel.RelativeAge;

import java.math.BigDecimal;

public class CalendarAgeUpdater extends AgeUpdater {

    public CalendarAgeUpdater(RelativeAge original, Period bugsData) {
        super(new CalendarAgeConverter(), original, bugsData);
    }

    @Override
    protected BigDecimal getOriginalBeginDate() {
        return original.getCalAgeOlder();
    }

    @Override
    protected void setBeginDate(BigDecimal convertedDate) {
        original.setCalAgeOlder(convertedDate);
    }

    @Override
    protected BigDecimal getOriginalEndDate() {
        return original.getCalAgeYounger();
    }

    @Override
    protected void setEndDate(BigDecimal convertedDate) {
        original.setCalAgeYounger(convertedDate);
    }
}
