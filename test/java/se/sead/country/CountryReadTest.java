package se.sead.country;

import org.junit.Test;
import se.sead.AccessReaderTest;
import se.sead.bugsimport.country.bugsmodel.Country;
import se.sead.bugsimport.country.bugsmodel.CountryBugsTable;

public class CountryReadTest extends AccessReaderTest<Country>{

    public CountryReadTest(){
        super("country");
    }

    @Test
    public void read(){
        readTableFromDefaultFolder(
                CountryExpectedTestDefinition.MDB_FILE,
                new CountryBugsTable(),
                CountryExpectedTestDefinition.EXPECTED_COUNTRIES
        );
    }

}
