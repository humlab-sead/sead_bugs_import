package se.sead.datescalendar;

import org.junit.Test;
import se.sead.AccessReaderTest;
import se.sead.bugsimport.datescalendar.bugsmodel.DatesCalendar;
import se.sead.bugsimport.datescalendar.bugsmodel.DatesCalendarBugsTable;

import java.util.Comparator;

public class ReadDatesCalendarTest extends AccessReaderTest<DatesCalendar> {

    public ReadDatesCalendarTest(){
        super("datescalendar");
    }

    @Test
    public void run(){
        readTableFromDefaultFolder(
                "datescalendar.mdb",
                new DatesCalendarBugsTable(),
                ExpectedBugsData.EXPECTED_DATA,
                new DatesCalendarComparator());
    }

    private static class DatesCalendarComparator implements Comparator<DatesCalendar> {
        @Override
        public int compare(DatesCalendar o1, DatesCalendar o2) {
            return o1.getCalendarCODE().compareTo(o2.getCalendarCODE());
        }
    }
}
