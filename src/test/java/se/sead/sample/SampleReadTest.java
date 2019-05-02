package se.sead.sample;

import org.junit.Test;
import se.sead.AccessReaderTest;
import se.sead.bugsimport.sample.bugsmodel.BugsSample;
import se.sead.bugsimport.sample.bugsmodel.SampleBugsTable;

import java.util.Comparator;

public class SampleReadTest extends AccessReaderTest<BugsSample> {

    public SampleReadTest(){
        super("sample");
    }

    @Test
    public void run(){
        readTableFromDefaultFolder(
                "sample.mdb",
                new SampleBugsTable(),
                ExpectedBugsData.EXPECTED_DATA,
                new BugsSampleComparator());
    }

    private static class BugsSampleComparator implements Comparator<BugsSample> {
        @Override
        public int compare(BugsSample o1, BugsSample o2) {
            return o1.getSampleCode().compareTo(o2.getSampleCode());
        }
    }
}
