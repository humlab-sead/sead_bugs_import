package se.sead.bugsimport.species.bugsmodel;

import com.healthmarketscience.jackcess.Row;
import se.sead.bugs.BugsTable;

/**
 * Created by erer0001 on 2016-04-22.
 */
public class INDEXBugsTable extends BugsTable<INDEX> {

    static final String TABLE_NAME = "INDEX";

    public INDEXBugsTable(){
        super(TABLE_NAME);
    }

    @Override
    public INDEX createItem(Row accessRow) {
        INDEX index = new INDEX();
        index.setCode(accessRow.getDouble("CODE"));
        index.setAuthority(accessRow.getString("AUTHORITY"));
        index.setFamily(accessRow.getString("FAMILY"));
        index.setGenus(accessRow.getString("GENUS"));
        index.setSpecies(accessRow.getString("SPECIES"));
        return index;
    }

}
