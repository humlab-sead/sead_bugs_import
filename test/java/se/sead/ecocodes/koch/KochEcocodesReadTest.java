package se.sead.ecocodes.koch;

import org.junit.Test;
import se.sead.AccessReaderTest;
import se.sead.bugsimport.ecocodes.koch.bugsmodel.EcoKoch;
import se.sead.bugsimport.ecocodes.koch.bugsmodel.EcoKochBugsTable;

import java.util.Comparator;
import java.util.Objects;

public class KochEcocodesReadTest extends AccessReaderTest<EcoKoch> {

    public KochEcocodesReadTest(){
        super("ecocodes/koch");
    }

    @Test
    public void run(){
        readTableFromDefaultFolder(
                "ecocodes.mdb",
                new EcoKochBugsTable(),
                ExpectedBugsData.EXPECTED_DATA,
                new EcoKochComparator()
                );
    }

    private static class EcoKochComparator implements Comparator<EcoKoch> {
        @Override
        public int compare(EcoKoch o1, EcoKoch o2) {
            if(Objects.equals(o1.getCode(), o2.getCode())){
                String o1KochCode = o1.getBugsKochCode();
                String o2KochCode = o2.getBugsKochCode();
                if(o1KochCode == null && o2KochCode == null){
                    return 0;
                } else if(o1KochCode != null && o2KochCode == null){
                    return -1;
                } else if(o1KochCode == null && o2KochCode != null){
                    return 1;
                } else {
                    return o1KochCode.compareTo(o2KochCode);
                }
            } else {
                return o1.getCode().compareTo(o2.getCode());
            }
        }
    }
}
