package se.sead.bibliography;

import org.junit.Test;
import se.sead.AccessReaderTest;
import se.sead.bugsimport.bibliography.bugsmodel.BugsBiblio;
import se.sead.bugsimport.bibliography.bugsmodel.BugsBiblioBugsTable;

import java.util.Comparator;

public class BibliographyReadTest extends AccessReaderTest<BugsBiblio> {

    public BibliographyReadTest(){super("bibliography");}

    @Test
    public void read(){
        readTableFromDefaultFolder(
                "bibliography.mdb",
                new BugsBiblioBugsTable(),
                ExpectedBugsData.EXPECTED_DATA,
                new BugsBibliographyComparator()
        );
    }

    private static class BugsBibliographyComparator implements Comparator<BugsBiblio> {
        @Override
        public int compare(BugsBiblio o1, BugsBiblio o2) {
            return o1.getReference().compareTo(o2.getReference());
        }
    }
}
