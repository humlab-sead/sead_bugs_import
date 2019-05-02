package se.sead.datesperiod;

import org.junit.Test;
import se.sead.AccessReaderTest;
import se.sead.bugsimport.datesperiod.bugsmodel.DatesPeriod;
import se.sead.bugsimport.datesperiod.bugsmodel.DatesPeriodBugsTable;
import se.sead.rdbcodes.*;

import java.util.Comparator;

public class DatesPeriodReadTest extends AccessReaderTest<DatesPeriod> {

    public DatesPeriodReadTest(){
        super("datesperiod");
    }

    @Test
    public void run(){
        readTableFromDefaultFolder(
                "datesperiod.mdb",
                new DatesPeriodBugsTable(),
                ExpectedBugsData.EXPECTED_DATA,
                new DatesPeriodComparator());
    }

    private static class DatesPeriodComparator implements Comparator<DatesPeriod> {
        @Override
        public int compare(DatesPeriod o1, DatesPeriod o2) {
            return o1.getPeriodDateCode().compareTo(o2.getPeriodDateCode());
        }
    }
}
