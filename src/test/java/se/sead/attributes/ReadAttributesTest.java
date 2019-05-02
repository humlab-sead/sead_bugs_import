package se.sead.attributes;

import org.junit.Test;
import se.sead.AccessReaderTest;
import se.sead.bugsimport.attributes.bugsmodel.AttributesBugsTable;
import se.sead.bugsimport.attributes.bugsmodel.BugsAttributes;

import java.util.Comparator;

public class ReadAttributesTest extends AccessReaderTest<BugsAttributes> {

    public ReadAttributesTest(){
        super("attributes");
    }

    @Test
    public void run(){
        readTableFromDefaultFolder(
                "attributes.mdb",
                new AttributesBugsTable(),
                ExpectedBugsData.EXPECTED_DATA,
                new BugsAttributesComparator());
    }

    private static class BugsAttributesComparator implements Comparator<BugsAttributes> {
        @Override
        public int compare(BugsAttributes o1, BugsAttributes o2) {
            return o1.compressToString().compareTo(o2.compressToString());
        }
    }
}
