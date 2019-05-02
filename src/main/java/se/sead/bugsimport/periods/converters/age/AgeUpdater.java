package se.sead.bugsimport.periods.converters.age;

import se.sead.bugsimport.periods.bugsmodel.Period;
import se.sead.bugsimport.periods.seadmodel.RelativeAge;
import se.sead.utils.BigDecimalDefinition;

import java.math.BigDecimal;

public abstract class AgeUpdater {

    private AgeConverter converter;
    protected RelativeAge original;
    protected Period bugsData;

    protected AgeUpdater(AgeConverter converter, RelativeAge original, Period bugsData) {
        this.converter = converter;
        this.original = original;
        this.bugsData = bugsData;
    }

    public final boolean setBeginDate(){
        BigDecimal originalBeginDate = getOriginalBeginDate();
        BigDecimal newBeginAge = converter.getBeginAge(bugsData);
        setBeginDate(newBeginAge);
        return !BigDecimalDefinition.equalBigDecimalNumericValues(originalBeginDate, newBeginAge);
    }

    protected abstract BigDecimal getOriginalBeginDate();
    protected abstract void setBeginDate(BigDecimal convertedDate);

    public final boolean setEndDate(){
        BigDecimal originalEndDate = getOriginalEndDate();
        BigDecimal newEndAge = converter.getEndAge(bugsData);
        setEndDate(newEndAge);
        return !BigDecimalDefinition.equalBigDecimalNumericValues(originalEndDate, newEndAge);
    }


    protected abstract BigDecimal getOriginalEndDate();
    protected abstract void setEndDate(BigDecimal convertedDate);

    public static AgeUpdater getUpdater(RelativeAge original, Period period){
        if(period.getYearsType() != null ){
            switch (period.getYearsType()){
                case "Calendar":
                case "calendar":
                case "Calender":
                case "calender": return new CalendarAgeUpdater(original, period);
                case "C14":
                case "c14":
                case "Radiometric":
                case "radiometric": return new C14AgeUpdater(original, period);
            }
        }
        return new UndeterminedAgeUpdater(original, period);
    }
}
