package se.sead.periods;

import org.junit.Test;
import se.sead.AccessReaderTest;
import se.sead.bugsimport.periods.bugsmodel.Period;
import se.sead.bugsimport.periods.bugsmodel.PeriodBugsTable;

import java.util.Comparator;

public class PeriodReadTest extends AccessReaderTest<Period> {

    public PeriodReadTest(){
        super("periods");
    }

    @Test
    public void run(){
        readTableFromDefaultFolder(
                "periods.mdb",
                new PeriodBugsTable(),
                ExpectedBugsData.EXPECTED_DATA,
                new PeriodComparator()
                );
    }

    private static class PeriodComparator  implements Comparator<Period> {
        @Override
        public int compare(Period o1, Period o2) {
            return o1.getPeriodCode().compareTo(o2.getPeriodCode());
        }
    }
}
