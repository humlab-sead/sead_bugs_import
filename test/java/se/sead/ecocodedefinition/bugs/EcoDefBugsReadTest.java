package se.sead.ecocodedefinition.bugs;

import org.junit.Test;
import se.sead.AccessReaderTest;
import se.sead.bugsimport.ecocodedefinition.bugs.bugsmodel.EcoDefBugs;
import se.sead.bugsimport.ecocodedefinition.bugs.bugsmodel.EcoDefBugsBugsTable;

import java.util.Comparator;

public class EcoDefBugsReadTest extends AccessReaderTest<EcoDefBugs> {

    public EcoDefBugsReadTest(){
        super("ecocodedefinition/bugs");
    }

    @Test
    public void run(){
        readTableFromDefaultFolder(
                "ecocodedefinition.mdb",
                new EcoDefBugsBugsTable(),
                ExpectedBugsData.EXPECTED_DATA,
                new EcoDefBugsComparator());
    }

    private static class EcoDefBugsComparator implements Comparator<EcoDefBugs> {
        @Override
        public int compare(EcoDefBugs o1, EcoDefBugs o2) {
            return o1.getBugsEcoCODE().compareTo(o2.getBugsEcoCODE());
        }
    }
}
