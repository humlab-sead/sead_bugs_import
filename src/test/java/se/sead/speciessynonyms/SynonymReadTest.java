package se.sead.speciessynonyms;

import org.junit.Test;
import se.sead.AccessReaderTest;
import se.sead.bugsimport.speciessynonyms.bugsmodel.Synonym;
import se.sead.bugsimport.speciessynonyms.bugsmodel.SynonymBugsTable;

import java.util.Comparator;

public class SynonymReadTest extends AccessReaderTest<Synonym> {

    public SynonymReadTest(){
        super("speciessynonyms");
    }

    @Test
    public void run(){
        readTableFromDefaultFolder(
                "speciessynonyms.mdb",
                new SynonymBugsTable(),
                ExpectedBugsData.EXPECTED_DATA,
                new SynonymComparator()
        );
    }

    private static class SynonymComparator implements Comparator<Synonym> {

        @Override
        public int compare(Synonym o1, Synonym o2) {
            return o1.compressToString().compareTo(o2.compressToString());
        }

    }
}
