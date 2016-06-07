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
                TaxaNotesImportTestDefinition.EXPECTED_READ_ITEMS,
                new TaxoNotesComparator());
    }

    private static class TaxoNotesComparator implements Comparator<TaxoNotes> {

        @Override
        public int compare(TaxoNotes o1, TaxoNotes o2) {
            int codeDifference = o1.getCode().compareTo(o2.getCode());
            if(codeDifference == 0){
                if(o1.getReference().equals(o2.getReference())){
                    return o1.getData().compareTo(o2.getData());
                } else {
                    o1.getReference().compareTo(o2.getReference());
                }
            }
            return codeDifference;
        }
    }
}
