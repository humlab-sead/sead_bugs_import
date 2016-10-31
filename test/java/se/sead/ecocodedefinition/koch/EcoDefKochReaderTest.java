package se.sead.ecocodedefinition.koch;

import org.junit.Test;
import se.sead.AccessReaderTest;
import se.sead.bugsimport.ecocodedefinition.koch.bugsmodel.EcoDefKoch;
import se.sead.bugsimport.ecocodedefinition.koch.bugsmodel.EcoDefKochBugsTable;
import se.sead.rdbcodes.*;

import java.util.Comparator;

public class EcoDefKochReaderTest extends AccessReaderTest<EcoDefKoch> {

    public EcoDefKochReaderTest(){
        super("ecocodedefinition/koch");
    }

    @Test
    public void run(){
        readTableFromDefaultFolder(
                "ecocodedefinition.mdb",
                new EcoDefKochBugsTable(),
                ExpectedBugsData.EXPECTED_DATA,
                new EcoDefKochComparator());
    }

    private static class EcoDefKochComparator implements Comparator<EcoDefKoch> {
        @Override
        public int compare(EcoDefKoch o1, EcoDefKoch o2) {
            return o1.getBugsKochCode().compareTo(o2.getBugsKochCode());
        }
    }
}
