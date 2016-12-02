package se.sead.species;

import org.junit.Test;
import se.sead.AccessReaderTest;
import se.sead.bugsimport.species.bugsmodel.INDEX;
import se.sead.bugsimport.species.bugsmodel.INDEXBugsTable;

import java.util.Comparator;

public class INDEXAccessReaderTest extends AccessReaderTest<INDEX> {

    public INDEXAccessReaderTest(){
        super("species");
    }

    @Test
    public void read(){
        readTableFromDefaultFolder(
                "INDEX.mdb",
                new INDEXBugsTable(),
                ExpectedBugsData.EXPECTED_ROW_DATA,
                new INDEXComparator());
    }

    private static class INDEXComparator implements Comparator<INDEX> {
        @Override
        public int compare(INDEX o1, INDEX o2) {
            return o1.getCode().compareTo(o2.getCode());
        }
    }
}
