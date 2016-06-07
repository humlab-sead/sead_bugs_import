package se.sead.mcr;

import org.junit.Test;
import se.sead.AccessReaderTest;
import se.sead.bugsimport.mcr.bugsmodel.MCRSummaryBugsTable;
import se.sead.bugsimport.mcr.bugsmodel.MCRSummaryData;

import java.util.Comparator;

public class McrSummaryReadTest extends AccessReaderTest<MCRSummaryData> {

    public McrSummaryReadTest(){
        super("mcr");
    }

    @Test
    public void read(){
        readTableFromDefaultFolder(
                "mcrsummary.mdb",
                new MCRSummaryBugsTable(),
                MCRSummarTestDefinition.EXPECTED_BUGS_DATA,
                new TestMCRSummaryDataComparator());
    }

    private static class TestMCRSummaryDataComparator implements Comparator<MCRSummaryData> {
        @Override
        public int compare(MCRSummaryData o1, MCRSummaryData o2) {
            return o1.getCode().compareTo(o2.getCode());
        }
    }
}
