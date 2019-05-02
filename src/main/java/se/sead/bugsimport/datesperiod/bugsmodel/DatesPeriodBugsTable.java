package se.sead.bugsimport.datesperiod.bugsmodel;

import com.healthmarketscience.jackcess.Row;
import se.sead.bugs.BugsTable;

public class DatesPeriodBugsTable extends BugsTable<DatesPeriod> {

    public static final String TABLE_NAME = "TDatesPeriod";

    public DatesPeriodBugsTable(){
        super(TABLE_NAME);
    }

    @Override
    public DatesPeriod createItem(Row accessRow) {
        DatesPeriod datesPeriod = new DatesPeriod();
        datesPeriod.setPeriodDateCode(accessRow.getString("PeriodDateCODE"));
        datesPeriod.setSampleCode(accessRow.getString("SampleCODE"));
        datesPeriod.setUncertainty(accessRow.getString("Uncertainty"));
        datesPeriod.setPeriodCode(accessRow.getString("PeriodCODE"));
        datesPeriod.setDatingMethod(accessRow.getString("DatingMethod"));
        datesPeriod.setNotes(accessRow.getString("Notes"));
        return datesPeriod;
    }
}
