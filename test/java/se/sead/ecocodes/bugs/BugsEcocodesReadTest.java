package se.sead.ecocodes.bugs;

import org.junit.Test;
import se.sead.AccessReaderTest;
import se.sead.bugsimport.ecocodes.bugs.bugsmodel.EcoBugs;
import se.sead.bugsimport.ecocodes.bugs.bugsmodel.EcoBugsBugsTable;

import java.util.Comparator;
import java.util.Objects;

public class BugsEcocodesReadTest extends AccessReaderTest<EcoBugs> {

    public BugsEcocodesReadTest(){
        super("ecocodes/bugs");
    }

    @Test
    public void run(){
        readTableFromDefaultFolder(
                "ecocodes.mdb",
                new EcoBugsBugsTable(),
                ExpectedBugsData.EXPECTED_DATA,
                new EcoBugsComparator()
        );
    }

    private static class EcoBugsComparator implements Comparator<EcoBugs> {
        @Override
        public int compare(EcoBugs o1, EcoBugs o2) {
            if(Objects.equals(o1.getCode(), o2.getCode())){
                String o1BugsCode = o1.getBugsEcoCode();
                String o2BugsCode = o2.getBugsEcoCode();
                if(o1BugsCode == null && o2BugsCode == null){
                    return 0;
                } else if(o1BugsCode != null && o2BugsCode == null){
                    return -1;
                } else if(o1BugsCode == null && o2BugsCode != null){
                    return 1;
                } else {
                    return o1BugsCode.compareTo(o2BugsCode);
                }
            } else {
                return o1.getCode().compareTo(o2.getCode());
            }
        }
    }
}
