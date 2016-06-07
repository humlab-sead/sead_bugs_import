package se.sead.bugsimport.specieskeys.bugsmodel;

import com.healthmarketscience.jackcess.Row;
import se.sead.bugs.BugsTable;

public class KeysBugsTable extends BugsTable<Keys> {

    static final String TABLE_NAME = "TKeys";

    public KeysBugsTable(){
        super(TABLE_NAME);
    }

    @Override
    public Keys createItem(Row accessRow) {
        Keys keys = new Keys();
        keys.setCode(accessRow.getDouble("CODE"));
        keys.setData(accessRow.getString("Data"));
        keys.setRef(accessRow.getString("Ref"));
        return keys;
    }
}
