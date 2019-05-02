package se.sead.bugsimport.datesradio.bugsmodel;

import com.healthmarketscience.jackcess.Row;
import se.sead.bugs.BugsTable;

public class DatesRadioBugsTable extends BugsTable<DatesRadio> {

    public static final String TABLE_NAME = "TDatesRadio";

    public DatesRadioBugsTable(){
        super(TABLE_NAME);
    }

    @Override
    public DatesRadio createItem(Row accessRow) {
        DatesRadio datesRadio = new DatesRadio();
        datesRadio.setDateCode(accessRow.getString("DateCODE"));
        datesRadio.setSampleCode(accessRow.getString("SampleCODE"));
        datesRadio.setLabNr(accessRow.getString("LabNr"));
        datesRadio.setUncertainty(accessRow.getString("Uncertainty"));
        datesRadio.setDate(accessRow.getInt("Date"));
        datesRadio.setAgeErrorOrPlusError(accessRow.getShort("AgeErrorOrPlusError"));
        datesRadio.setAgeErrorMinus(accessRow.getInt("AgeErrorMinus"));
        datesRadio.setDatingMethod(accessRow.getString("DatingMethod"));
        datesRadio.setMaterialType(accessRow.getString("MaterialType"));
        datesRadio.setLabId(accessRow.getString("LabID"));
        datesRadio.setNotes(accessRow.getString("Notes"));
        return datesRadio;
    }
}
