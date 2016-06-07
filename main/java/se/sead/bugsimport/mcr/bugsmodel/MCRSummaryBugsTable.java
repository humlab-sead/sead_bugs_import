package se.sead.bugsimport.mcr.bugsmodel;

import com.healthmarketscience.jackcess.Row;
import se.sead.bugs.BugsTable;

public class MCRSummaryBugsTable extends BugsTable<MCRSummaryData> {

    static final String TABLE_NAME = "TMCRSummaryData";

    public MCRSummaryBugsTable(){
        super(TABLE_NAME);
    }

    @Override
    public MCRSummaryData createItem(Row accessRow) {
        MCRSummaryData summary = new MCRSummaryData();
        summary.setCode(accessRow.getDouble("CODE"));
        summary.setMaxLo(accessRow.getShort("TMaxLo"));
        summary.setMaxHi(accessRow.getShort("TMaxHi"));
        summary.setMinLo(accessRow.getShort("TMinLo"));
        summary.setMinHi(accessRow.getShort("TMinHi"));
        summary.setRangeLo(accessRow.getShort("TRangeLo"));
        summary.setRangeHi(accessRow.getShort("TRangeHi"));
        summary.setCogMidTMax(accessRow.getShort("COGMidTMax"));
        summary.setCogMidTRange(accessRow.getShort("COGMidTRange"));
        return summary;
    }
}
