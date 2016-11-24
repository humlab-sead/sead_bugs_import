package se.sead.bugsimport.sample.bugsmodel;

import com.healthmarketscience.jackcess.Row;
import se.sead.bugs.BugsTable;

public class SampleBugsTable extends BugsTable<BugsSample> {

    public static final String TABLE_NAME = "TSample";

    public SampleBugsTable(){
        super(TABLE_NAME);
    }

    @Override
    public BugsSample createItem(Row accessRow) {
        BugsSample sample = new BugsSample();
        sample.setSampleCode(accessRow.getString("SampleCODE"));
        sample.setSiteCode(accessRow.getString("SiteCODE"));
        sample.setCountSheetCode(accessRow.getString("CountsheetCODE"));
        sample.setX(accessRow.getString("X"));
        sample.setY(accessRow.getString("Y"));
        sample.setRefNrContext(accessRow.getString("RefNrContext"));
        sample.setZOrDepthTop(accessRow.getDouble("ZorDepthTop"));
        sample.setZOrDepthBot(accessRow.getDouble("ZorDepthBot"));
        return sample;
    }
}
