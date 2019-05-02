package se.sead.taxanotes;

import org.junit.Test;
import se.sead.AccessReaderTest;
import se.sead.bugsimport.taxanotes.bugsmodel.TaxoNotes;
import se.sead.bugsimport.taxanotes.bugsmodel.TaxoNotesBugsTable;

import java.util.Comparator;

public class TaxaNotesReadTest extends AccessReaderTest<TaxoNotes> {

    public TaxaNotesReadTest(){
        super("taxanotes");
    }

    @Test
    public void readTable(){
        super.readTableFromDefaultFolder(
                "taxanotes.mdb",
                new TaxoNotesBugsTable(),
                ExpectedBugsData.EXPECTED_DATA,
                new TaxoNotesComparator());
    }

    private static class TaxoNotesComparator implements Comparator<TaxoNotes> {

        @Override
        public int compare(TaxoNotes o1, TaxoNotes o2) {
            if(o1.getCode() == null && o2.getCode() == null){
                return 0;
            } else if(o1.getCode() != null && o2.getCode() == null){
                return -1;
            } else if(o1.getCode() == null && o2.getCode() != null){
                return 1;
            } else {
                return o1.getCode().compareTo(o2.getCode());
            }
        }
    }
}
