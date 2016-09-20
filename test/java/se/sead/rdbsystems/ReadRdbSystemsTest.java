package se.sead.rdbsystems;

import org.junit.Test;
import se.sead.AccessReaderTest;
import se.sead.bugsimport.rdbsystems.bugsmodel.BugsRDBSystem;
import se.sead.bugsimport.rdbsystems.bugsmodel.RDBSystemBugsTable;

import java.util.Comparator;

public class ReadRdbSystemsTest extends AccessReaderTest<BugsRDBSystem> {

    public ReadRdbSystemsTest() {
        super("rdbsystems");
    }

    @Test
    public void run(){
        readTableFromDefaultFolder("rdbsystems.mdb", new RDBSystemBugsTable(), ExpectedBugsData.EXPECTED_DATA, new RDBSystemComparator());
    }

    private static class RDBSystemComparator implements Comparator<BugsRDBSystem> {
        @Override
        public int compare(BugsRDBSystem o1, BugsRDBSystem o2) {
            return o1.getRdbSystemCode().compareTo(o2.getRdbSystemCode());
        }
    }
}
