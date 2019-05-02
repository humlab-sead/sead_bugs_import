package se.sead.bugsimport.speciesdistribution.bugsmodel;

import com.healthmarketscience.jackcess.Row;
import se.sead.bugs.BugsTable;

public class DistribBugsTable extends BugsTable<Distrib> {

    static final String TABLE_NAME = "TDistrib";

    public DistribBugsTable(){
        super(TABLE_NAME);
    }

    @Override
    public Distrib createItem(Row accessRow) {
        Distrib distrib = new Distrib();
        distrib.setCode(accessRow.getDouble("CODE"));
        distrib.setData(accessRow.getString("Data"));
        distrib.setRef(accessRow.getString("Ref"));
        return distrib;
    }
}
