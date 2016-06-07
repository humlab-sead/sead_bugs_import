package se.sead.bugs;

import com.healthmarketscience.jackcess.Row;

/**
 * Created by erer0001 on 2016-04-22.
 */
public abstract class BugsTable<T> {

    private String tableName;

    protected BugsTable(String tableName){
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

    public abstract T createItem(Row accessRow);

}
