package se.sead.speciesdistribution;

import org.junit.Test;
import se.sead.AccessReaderTest;
import se.sead.bugsimport.speciesdistribution.bugsmodel.Distrib;
import se.sead.bugsimport.speciesdistribution.bugsmodel.DistribBugsTable;

import java.util.Comparator;

public class ReadTest extends AccessReaderTest<Distrib> {

    public ReadTest(){
        super("speciesdistribution");
    }

    @Test
    public void read(){
        readTableFromDefaultFolder(
                "speciesdistribution.mdb",
                new DistribBugsTable(),
                SpeciesDistributionTestDefinition.EXPECTED_BUGS_ITEMS,
                new TestDistribComparator());
    }

    private static class TestDistribComparator implements Comparator<Distrib>{
        @Override
        public int compare(Distrib o1, Distrib o2) {
            int codeDifference = o1.getCode().compareTo(o2.getCode());
            if(codeDifference == 0){
                return o1.getData().compareTo(o2.getData());
            }
            return codeDifference;
        }
    }
}
