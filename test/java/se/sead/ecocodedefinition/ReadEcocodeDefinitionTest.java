package se.sead.ecocodedefinition;

import org.junit.Test;
import se.sead.AccessReaderTest;
import se.sead.bugsimport.ecocodedefinition.bugsmodel.EcoDefKoch;
import se.sead.bugsimport.ecocodedefinition.bugsmodel.EcoDefKochBugsTable;

import java.util.Comparator;

public class ReadEcocodeDefinitionTest extends AccessReaderTest<EcoDefKoch> {

    public ReadEcocodeDefinitionTest() {
        super("ecocodedefinition");
    }

    @Test
    public void run(){
        readTableFromDefaultFolder(
                "ecocodedefinition.mdb",
                new EcoDefKochBugsTable(),
                ExpectedBugsAccessData.EXPECTED_DATA,
                new EcoDefKochComparator()
                );
    }

    private static class EcoDefKochComparator implements Comparator<EcoDefKoch> {
        @Override
        public int compare(EcoDefKoch o1, EcoDefKoch o2) {
            return o1.getBugsKochCode().compareTo(o2.getBugsKochCode());
        }
    }
}
