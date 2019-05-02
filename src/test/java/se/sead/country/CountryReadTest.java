package se.sead.country;

import org.junit.Test;
import se.sead.AccessReaderTest;
import se.sead.bugsimport.locations.country.bugsmodel.Country;
import se.sead.bugsimport.locations.country.bugsmodel.CountryBugsTable;

public class CountryReadTest extends AccessReaderTest<Country>{

    public CountryReadTest(){
        super("country");
    }

    @Test
    public void read(){
        readTableFromDefaultFolder(
                "countries.mdb",
                new CountryBugsTable(),
                ExpectedBugsData.EXPECTED_COUNTRIES
        );
    }

}
