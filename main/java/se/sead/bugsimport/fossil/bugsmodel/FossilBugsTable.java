package se.sead.bugsimport.fossil.bugsmodel;

import com.healthmarketscience.jackcess.Row;
import se.sead.bugs.BugsTable;

public class FossilBugsTable extends BugsTable<Fossil> {

    public static final String TABLE_NAME = "TFossil";

    public FossilBugsTable(){
        super(TABLE_NAME);
    }

    @Override
    public Fossil createItem(Row accessRow) {
        Fossil fossil = new Fossil();
        fossil.setSampleCODE(accessRow.getString("SampleCODE"));
        fossil.setFossilBugsCODE(accessRow.getString("FossilBugsCODE"));
        fossil.setCode(accessRow.getDouble("CODE"));
        fossil.setAbundance(accessRow.getInt("Abundance"));
        return fossil;
    }
}
