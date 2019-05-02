package se.sead.countsheets;

import org.junit.Test;
import se.sead.AccessReaderTest;
import se.sead.bugsimport.countsheets.bugsmodel.Countsheet;
import se.sead.bugsimport.countsheets.bugsmodel.CountsheetBugsTable;

import java.util.Comparator;

public class CountSheetReadTest extends AccessReaderTest<Countsheet> {

    public CountSheetReadTest() {
        super("countsheets");
    }

    @Test
    public void run(){
        readTableFromDefaultFolder(
                "countsheets.mdb",
                new CountsheetBugsTable(),
                ExpectedBugsData.EXPECTED_DATA,
                new CountsheetComparator()
        );
    }

    private static class CountsheetComparator implements Comparator<Countsheet> {
        @Override
        public int compare(Countsheet o1, Countsheet o2) {
            return o1.compressToString().compareTo(o2.compressToString());
        }
    }
}
