package se.sead.mcr;

import org.junit.Test;
import se.sead.AccessReaderTest;
import se.sead.bugsimport.mcr.bugsmodel.BirmBeetleDat;
import se.sead.bugsimport.mcr.bugsmodel.BirmBeetleDatBugsTable;

import java.util.Comparator;

/**
 * Created by erer0001 on 2016-05-12.
 */
public class BirmBeetleDatAccessReaderTest extends AccessReaderTest<BirmBeetleDat> {

    public BirmBeetleDatAccessReaderTest(){
        super("mcr");
    }

    @Test
    public void read(){
        readTableFromDefaultFolder(
                BirmBeetleTestDefinition.BUGS_DATA_FILE,
                new BirmBeetleDatBugsTable(),
                BirmBeetleTestDefinition.EXPECTED_ROW_DATA,
                new TestBirmBeetleDatComparator());
    }

    private static class TestBirmBeetleDatComparator implements Comparator<BirmBeetleDat> {

        @Override
        public int compare(BirmBeetleDat o1, BirmBeetleDat o2) {
            int codeDifference = o1.getBugsCode().compareTo(o2.getBugsCode());
            if(codeDifference == 0){
                return o1.getRow().compareTo(o2.getRow());
            }
            return codeDifference;
        }
    }
}
