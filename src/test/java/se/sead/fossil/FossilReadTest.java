package se.sead.fossil;

import org.junit.Test;
import se.sead.AccessReaderTest;
import se.sead.bugsimport.fossil.bugsmodel.Fossil;
import se.sead.bugsimport.fossil.bugsmodel.FossilBugsTable;

import java.util.Comparator;

public class FossilReadTest extends AccessReaderTest<Fossil> {

    public FossilReadTest(){
        super("fossil");
    }

    @Test
    public void run(){
        readTableFromDefaultFolder(
                "fossil.mdb",
                new FossilBugsTable(),
                ExpectedBugsData.EXPECTED_DATA,
                new FossilComparator());
    }

    private static class FossilComparator implements Comparator<Fossil> {
        @Override
        public int compare(Fossil o1, Fossil o2) {
            return o1.getFossilBugsCODE().compareTo(o2.getFossilBugsCODE());
        }
    }
}
