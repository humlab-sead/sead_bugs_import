package se.sead.bugsimport.periods.converters.age;

import se.sead.bugsimport.periods.bugsmodel.Period;
import se.sead.bugsimport.periods.seadmodel.RelativeAge;

import java.math.BigDecimal;

public class C14AgeUpdater extends AgeUpdater {

    public C14AgeUpdater(RelativeAge original, Period bugsData) {
        super(new C14AgeConverter(), original, bugsData);
    }

    @Override
    protected BigDecimal getOriginalBeginDate() {
        return original.getC14AgeOlder();
    }

    @Override
    protected void setBeginDate(BigDecimal convertedDate) {
        original.setC14AgeOlder(convertedDate);
    }

    @Override
    protected BigDecimal getOriginalEndDate() {
        return original.getC14AgeYounger();
    }

    @Override
    protected void setEndDate(BigDecimal convertedDate) {
        original.setC14AgeYounger(convertedDate);
    }
}
