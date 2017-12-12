package se.sead.bugsimport.datescalendar.converters;

import se.sead.bugsimport.datescalendar.bugsmodel.DatesCalendar;
import se.sead.bugsimport.periods.bugsmodel.Period;

public abstract class AbstractPeriodForRelativeAgeCreator {

    private static final String STANDARD_NOTES = "Autocreated from bugs import";

    protected DatesCalendar datesCalendar;
    protected String computedAbbreviation;

    public AbstractPeriodForRelativeAgeCreator(DatesCalendar datesCalendar, String computedAbbreviation) {
        this.datesCalendar = datesCalendar;
        this.computedAbbreviation = computedAbbreviation;
    }

    public Period create(){
        Period period = new Period();
        setAgePeriodData(period);
        period.setType(getType());
        period.setDesc(STANDARD_NOTES);
        period.setPeriodCode(computedAbbreviation);
        period.setYearsType(getYearsType());
        return period;
    }

    protected abstract void setAgePeriodData(Period period);

    protected abstract String getType();
    protected abstract String getYearsType();
}
