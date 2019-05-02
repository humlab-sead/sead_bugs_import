package se.sead.bugsimport.locations.country.bugsmodel;

import com.healthmarketscience.jackcess.Row;
import se.sead.bugs.BugsTable;

public class CountryBugsTable extends BugsTable<Country> {

    static final String TABLE_NAME = "TCountry";

    public CountryBugsTable(){
        super(TABLE_NAME);
    }

    @Override
    public Country createItem(Row accessRow) {
        Country country = new Country();
        country.setCountry(accessRow.getString("Country"));
        country.setCountryCode(accessRow.getString("CountryCode"));
        return country;
    }
}
