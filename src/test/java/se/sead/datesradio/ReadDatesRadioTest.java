package se.sead.datesradio;

import org.junit.Test;
import se.sead.AccessReaderTest;
import se.sead.bugsimport.datesradio.bugsmodel.DatesRadio;
import se.sead.bugsimport.datesradio.bugsmodel.DatesRadioBugsTable;

public class ReadDatesRadioTest extends AccessReaderTest<DatesRadio>{

    public ReadDatesRadioTest(){
        super("datesradio");
    }

    @Test
    public void run(){
        readTableFromDefaultFolder(
                "datesradio.mdb",
                new DatesRadioBugsTable(),
                ExpectedBugsData.EXPECTED_DATA,
                (o1, o2) -> o1.getDateCode().compareTo(o2.getDateCode())
        );
    }
}
