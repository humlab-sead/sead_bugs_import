package se.sead.mcrnames;

import org.junit.Test;
import se.sead.AccessReaderTest;
import se.sead.bugsimport.mcrnames.bugsmodel.BugsMCRNames;
import se.sead.bugsimport.mcrnames.bugsmodel.MCRNamesBugsTable;

import java.util.Comparator;

public class ReadMcrNumberTest extends AccessReaderTest<BugsMCRNames> {

    public ReadMcrNumberTest(){
        super("mcrnames");
    }

    @Test
    public void run(){
        readTableFromDefaultFolder(
                "mcrnames.mdb",
                new MCRNamesBugsTable(),
                ExpectedBugsData.EXPECTED_DATA,
                new MCRNamesComparator());
    }

    private static class MCRNamesComparator implements Comparator<BugsMCRNames> {
        @Override
        public int compare(BugsMCRNames o1, BugsMCRNames o2) {
            return o1.getCode().compareTo(o2.getCode());
        }
    }
}
