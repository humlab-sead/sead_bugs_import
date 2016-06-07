package se.sead.speciesbiology;

import org.junit.Test;
import se.sead.AccessReaderTest;
import se.sead.bugsimport.speciesbiology.bugsmodel.Biology;
import se.sead.bugsimport.speciesbiology.bugsmodel.BiologyBugsTable;

public class TextBiologyReadTest extends AccessReaderTest<Biology>{

    public TextBiologyReadTest(){
        super("speciesbiology");
    }

    @Test
    public void readBiologyData(){
        readTableFromDefaultFolder(
                SpeciesBiologyTestDefinition.ACCESS_DATABASE_FILE,
                new BiologyBugsTable(),
                SpeciesBiologyTestDefinition.EXPECTED_ACCESS_DATA);

    }
}
