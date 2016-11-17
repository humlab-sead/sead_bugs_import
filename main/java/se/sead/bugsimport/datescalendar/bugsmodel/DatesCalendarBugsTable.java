package se.sead.bugsimport.datescalendar.bugsmodel;

import com.healthmarketscience.jackcess.Row;
import se.sead.bugs.BugsTable;

public class DatesCalendarBugsTable extends BugsTable<DatesCalendar> {

    public static final String TABLE_NAME = "TDatesCalendar";

    public DatesCalendarBugsTable(){
        super(TABLE_NAME);
    }

    @Override
    public DatesCalendar createItem(Row accessRow) {
        DatesCalendar datesCalendar = new DatesCalendar();
        datesCalendar.setSample(accessRow.getString("SampleCODE"));
        datesCalendar.setUncertainty(accessRow.getString("Uncertainty"));
        datesCalendar.setCalendarCODE(accessRow.getString("CalendarCODE"));
        datesCalendar.setDate(accessRow.getInt("Date"));
        datesCalendar.setBcadbp(accessRow.getString("BCADBP"));
        datesCalendar.setDatingMethod(accessRow.getString("DatingMethod"));
        datesCalendar.setNotes(accessRow.getString("Notes"));
        return datesCalendar;
    }
}
