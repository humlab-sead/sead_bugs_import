package se.sead.bugsimport.attributes.bugsmodel;

import com.healthmarketscience.jackcess.Row;
import se.sead.bugs.BugsTable;

public class AttributesBugsTable extends BugsTable<BugsAttributes> {

    public static final String TABLE_NAME = "TAttributes";

    public AttributesBugsTable(){
        super(TABLE_NAME);
    }

    @Override
    public BugsAttributes createItem(Row accessRow) {
        BugsAttributes attributes = new BugsAttributes();
        attributes.setCode(accessRow.getDouble("CODE"));
        attributes.setType(accessRow.getString("AttribType"));
        attributes.setMeasure(accessRow.getString("AttribMeasure"));
        attributes.setValue(accessRow.getFloat("Value"));
        attributes.setUnits(accessRow.getString("AttribUnits"));
        return attributes;
    }
}
