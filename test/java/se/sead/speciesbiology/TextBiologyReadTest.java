package se.sead.speciesbiology;

import org.junit.Test;
import se.sead.AccessReaderTest;
import se.sead.bugsimport.speciesbiology.bugsmodel.Biology;
import se.sead.bugsimport.speciesbiology.bugsmodel.BiologyBugsTable;

import java.util.Comparator;

public class TextBiologyReadTest extends AccessReaderTest<Biology>{

    public TextBiologyReadTest(){
        super("speciesbiology");
    }

    @Test
    public void readBiologyData(){
        readTableFromDefaultFolder(
                "biology.mdb",
                new BiologyBugsTable(),
                ExpectedBugsData.EXPECTED_DATA,
                new BiologyComparator());
    }

    private static class BiologyComparator implements Comparator<Biology>{
        @Override
        public int compare(Biology o1, Biology o2) {
            String o1Value = o1.getCode() + o1.getRef();
            String o2Value = o2.getCode() + o2.getRef();
            return o1Value.compareTo(o2Value);
        }
    }
}
