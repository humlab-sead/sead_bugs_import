package se.sead.rdb;

import org.junit.Test;
import se.sead.AccessReaderTest;
import se.sead.bugsimport.rdb.bugsmodel.BugsRDB;
import se.sead.bugsimport.rdb.bugsmodel.RDBBugsTable;

import java.util.Comparator;

public class RdbReadTest extends AccessReaderTest<BugsRDB>{

    public RdbReadTest(){
        super("rdb");
    }

    @Test
    public void run(){
        readTableFromDefaultFolder(
                "rdb.mdb",
                new RDBBugsTable(),
                ExpectedBugData.EXPECTED_DATA,
                new BugsRDBComparator()
        );
    }

    private static class BugsRDBComparator implements Comparator<BugsRDB> {
        @Override
        public int compare(BugsRDB o1, BugsRDB o2) {
            return o1.compressToString().compareTo(o2.compressToString());
        }
    }
}
