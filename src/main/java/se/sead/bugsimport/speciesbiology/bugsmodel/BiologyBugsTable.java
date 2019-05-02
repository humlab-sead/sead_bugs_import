package se.sead.bugsimport.speciesbiology.bugsmodel;

import com.healthmarketscience.jackcess.Row;
import se.sead.bugs.BugsTable;

/**
 * Created by erer0001 on 2016-05-18.
 */
public class BiologyBugsTable extends BugsTable<Biology> {

    static final String TABLE_NAME = "TBiology";

    public BiologyBugsTable() {
        super(TABLE_NAME);
    }

    @Override
    public Biology createItem(Row accessRow) {
        Biology biology = new Biology();
        biology.setCode(accessRow.getDouble("CODE"));
        biology.setRef(accessRow.getString("Ref"));
        biology.setData(accessRow.getString("Data"));
        return biology;
    }
}
