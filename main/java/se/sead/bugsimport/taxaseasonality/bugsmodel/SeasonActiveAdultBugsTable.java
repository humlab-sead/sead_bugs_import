package se.sead.bugsimport.taxaseasonality.bugsmodel;

import com.healthmarketscience.jackcess.Row;
import se.sead.bugs.BugsTable;

public class SeasonActiveAdultBugsTable extends BugsTable<SeasonActiveAdult> {

    public static final String TABLE_NAME = "TSeasonActiveAdult";

    public SeasonActiveAdultBugsTable(){
        super(TABLE_NAME);
    }

    @Override
    public SeasonActiveAdult createItem(Row accessRow) {
        SeasonActiveAdult seasonActiveAdult = new SeasonActiveAdult();
        seasonActiveAdult.setCode(accessRow.getDouble("CODE"));
        seasonActiveAdult.setSeason(accessRow.getString("HSeason"));
        seasonActiveAdult.setCountryCode(accessRow.getString("CountryCode"));
        return seasonActiveAdult;
    }
}
