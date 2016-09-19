package se.sead.ecocodedefinitiongroups;

import org.junit.Test;
import se.sead.AccessReaderTest;
import se.sead.bugsimport.ecocodedefinitiongroups.bugsmodel.EcoDefGroups;
import se.sead.bugsimport.ecocodedefinitiongroups.bugsmodel.EcoDefGroupsBugsTable;

import java.util.Comparator;

public class ReadEcocodeDefinitionGroupTest extends AccessReaderTest<EcoDefGroups> {

    public ReadEcocodeDefinitionGroupTest(){
        super("ecocodedefinitiongroups");
    }
    public void run(){
        readTableFromDefaultFolder(
                "ecocodedefinitiongroups.mdb",
                new EcoDefGroupsBugsTable(),
                BugsExpectedData.EXPECTED_ACCESS_DATA,
                new EcoDefGroupComparator());
    }

    private static class EcoDefGroupComparator implements Comparator<EcoDefGroups>{
        @Override
        public int compare(EcoDefGroups o1, EcoDefGroups o2) {
            if(o1.getEcoGroupCode() != null && o2.getEcoGroupCode() != null) {
                return o1.getEcoGroupCode().compareTo(o2.getEcoGroupCode());
            } else if(o1.getEcoName() != null && o2.getEcoName() != null){
                return o1.getEcoName().compareTo(o2.getEcoName());
            } else {
                return 0;
            }
        }
    }
}
