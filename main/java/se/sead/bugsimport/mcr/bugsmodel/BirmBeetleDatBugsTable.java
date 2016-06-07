package se.sead.bugsimport.mcr.bugsmodel;

import com.healthmarketscience.jackcess.Row;
import se.sead.bugs.BugsTable;

/**
 * Created by erer0001 on 2016-05-12.
 */
public class BirmBeetleDatBugsTable extends BugsTable<BirmBeetleDat> {

    static final String TABLE_NAME = "TbirmBEETLEdat";

    public BirmBeetleDatBugsTable() {
        super(TABLE_NAME);
    }

    @Override
    public BirmBeetleDat createItem(Row accessRow) {
        BirmBeetleDat data = new BirmBeetleDat();
        data.setRow(accessRow.getShort("MCRRow"));
        data.setBugsCode(accessRow.getDouble("CODE"));
        for (int i = 1; i <= 60; i++) {
            String fieldName = "Field" + i;
            data.setFieldData(i, accessRow.getString(fieldName));
        }
        return data;
    }
}
