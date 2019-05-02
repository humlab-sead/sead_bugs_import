package se.sead.rdbcodes;

import org.junit.Test;
import se.sead.AccessReaderTest;
import se.sead.bugsimport.rdbcodes.bugsmodel.BugsRDBCodes;
import se.sead.bugsimport.rdbcodes.bugsmodel.RDBCodesBugsTable;

import java.util.Comparator;

public class RDBCodesReaderTest extends AccessReaderTest<BugsRDBCodes>{

    public RDBCodesReaderTest(){
        super("rdbcodes");
    }

    @Test
    public void run(){
        readTableFromDefaultFolder(
                "rdbcodes.mdb",
                new RDBCodesBugsTable(),
                ExpectedBugsData.EXPECTED_DATA,
                new BugsRDBCodesComparator());
    }

    private static class BugsRDBCodesComparator implements Comparator<BugsRDBCodes>{
        @Override
        public int compare(BugsRDBCodes o1, BugsRDBCodes o2) {
            return o1.getRdbCode().compareTo(o2.getRdbCode());
        }
    }
}
