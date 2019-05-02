package se.sead.bugsimport.periods.bugsmodel;

import com.healthmarketscience.jackcess.Row;
import se.sead.bugs.BugsTable;

public class PeriodBugsTable extends BugsTable<Period> {

    public static final String TABLE_NAME = "TPeriods";

    static final String BUGS_DATA_DELIMITER = "|";

    public PeriodBugsTable(){
        super(TABLE_NAME);
    }

    @Override
    public Period createItem(Row accessRow) {
        Period period = new Period();
        period.setPeriodCode(accessRow.getString("PeriodCODE"));
        period.setName(accessRow.getString("PeriodName"));
        period.setType(accessRow.getString("PeriodType"));
        period.setDesc(accessRow.getString("PeriodDesc"));
        period.setRef(accessRow.getString("PeriodRef"));
        period.setGeography(accessRow.getString("PeriodGeog"));
        period.setBegin(accessRow.getInt("Begin"));
        period.setBeginBCad(accessRow.getString("BeginBCAD"));
        period.setEnd(accessRow.getInt("End"));
        period.setEndBCad(accessRow.getString("EndBCAD"));
        period.setYearsType(accessRow.getString("YearsType"));
        return period;
    }
}
