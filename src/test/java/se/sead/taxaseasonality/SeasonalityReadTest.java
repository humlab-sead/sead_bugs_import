package se.sead.taxaseasonality;

import org.junit.Test;
import se.sead.AccessReaderTest;
import se.sead.bugsimport.taxaseasonality.bugsmodel.SeasonActiveAdult;
import se.sead.bugsimport.taxaseasonality.bugsmodel.SeasonActiveAdultBugsTable;

import java.util.Comparator;

public class SeasonalityReadTest extends AccessReaderTest<SeasonActiveAdult> {

    public SeasonalityReadTest(){
        super("taxaseasonality");
    }

    @Test
    public void run(){
        readTableFromDefaultFolder(
                "taxaseasonality.mdb",
                new SeasonActiveAdultBugsTable(),
                ImportTestDefinition.EXPECTED_BUGS_DATA,
                new SeasonActiveAdultComparator());
    }

    private static class SeasonActiveAdultComparator implements Comparator<SeasonActiveAdult> {
        @Override
        public int compare(SeasonActiveAdult o1, SeasonActiveAdult o2) {
            int comparisonResult = o1.getCode().compareTo(o2.getCode());
            if(comparisonResult != 0){
                return comparisonResult;
            }
            comparisonResult = compare(o1.getSeason(), o2.getSeason());
            return comparisonResult == 0 ?
                    comparisonResult :
                    compare(o1.getCountryCode(), o2.getCountryCode());
        }

        private int compare(String o1, String o2){
            if(o1 != null && o2 != null){
                return o1.compareTo(o2);
            } else if(o1 != null && o2 == null){
                return 1;
            } else if(o1 == null && o2 != null){
                return -1;
            } else {
                return 0;
            }
        }
    }
}
