package se.sead.specieskeys;

import org.junit.Test;
import se.sead.AccessReaderTest;
import se.sead.bugsimport.specieskeys.bugsmodel.Keys;
import se.sead.bugsimport.specieskeys.bugsmodel.KeysBugsTable;

import java.util.Comparator;

public class SpeciesKeysReadTest extends AccessReaderTest<Keys> {

    public SpeciesKeysReadTest(){
        super("specieskeys");
    }

    @Test
    public void read(){
        readTableFromDefaultFolder(
                "specieskeys.mdb",
                new KeysBugsTable(),
                KeysTestDefinition.EXPECTED_READ_ITEMS,
                new TestKeysComparator());
    }

    private static class TestKeysComparator implements Comparator<Keys> {
        @Override
        public int compare(Keys o1, Keys o2) {
            int codeDifference = o1.getCode().compareTo(o2.getCode());
            if(codeDifference == 0){
                return o1.getData().compareTo(o2.getData());
            }
            return codeDifference;
        }
    }
}
