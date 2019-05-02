package se.sead.lab;

import org.junit.Test;
import se.sead.AccessReaderTest;
import se.sead.bugsimport.lab.bugsmodel.BugsLab;
import se.sead.bugsimport.lab.bugsmodel.LabBugsTable;

import java.util.Comparator;

public class ReadLabTest extends AccessReaderTest<BugsLab>{

    public ReadLabTest(){
        super("lab");
    }

    @Test
    public void run(){
        readTableFromDefaultFolder(
                "lab.mdb",
                new LabBugsTable(),
                ExpectedBugsData.EXPECTED_DATA,
                new BugsLabComparator());
    }

    private static class BugsLabComparator implements Comparator<BugsLab> {
        @Override
        public int compare(BugsLab o1, BugsLab o2) {
            return o1.getLabId().compareTo(o2.getLabId());
        }
    }
}
