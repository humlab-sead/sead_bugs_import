package se.sead.species;

import org.junit.Test;
import se.sead.AccessReaderTest;
import se.sead.bugsimport.species.bugsmodel.INDEX;
import se.sead.bugsimport.species.bugsmodel.INDEXBugsTable;

/**
 * Created by erer0001 on 2016-05-12.
 */
public class INDEXAccessReaderTest extends AccessReaderTest<INDEX> {

    public INDEXAccessReaderTest(){
        super("species");
    }

    @Test
    public void read(){
        readTableFromDefaultFolder(
                INDEXAccessDatabaseTestDefinition.DATA_FILE,
                new INDEXBugsTable(),
                INDEXAccessDatabaseTestDefinition.EXPECTED_ROW_DATA);
    }
}
