package se.sead.country;

import se.sead.bugsimport.locations.country.bugsmodel.Country;

import java.util.Arrays;
import java.util.List;

class ExpectedBugsData {

    static final List<Country> EXPECTED_COUNTRIES =
            Arrays.asList(
                    create("Eg", "Egypt"),
                    create("Int", "International"),
                    create("Swe", null),
                    create("Ger", "Germany")
            );

    private static Country create(String code, String name){
        Country country = new Country();
        country.setCountryCode(code);
        country.setCountry(name);
        return country;
    }
}
