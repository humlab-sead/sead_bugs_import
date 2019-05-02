package se.sead.speciesassociation;

import org.junit.Test;
import se.sead.AccessReaderTest;
import se.sead.bugsimport.speciesassociation.bugsmodel.BugsSpeciesAssociation;
import se.sead.bugsimport.speciesassociation.bugsmodel.SpeciesAssociationBugsTable;

import java.util.Comparator;

public class SpeciesAssociationReadTest extends AccessReaderTest<BugsSpeciesAssociation> {

    public SpeciesAssociationReadTest(){
        super("speciesassociation");
    }

    @Test
    public void run(){
        readTableFromDefaultFolder(
                "speciesassociation.mdb",
                new SpeciesAssociationBugsTable(),
                ExpectedBugsData.EXPECTED_DATA,
                new BugsSpeciesAssociationComparator()
                );
    }

    private static final class BugsSpeciesAssociationComparator implements Comparator<BugsSpeciesAssociation> {
        @Override
        public int compare(BugsSpeciesAssociation o1, BugsSpeciesAssociation o2) {
            return o1.getSpeciesAssociationID().compareTo(o2.getSpeciesAssociationID());
        }
    }
}
