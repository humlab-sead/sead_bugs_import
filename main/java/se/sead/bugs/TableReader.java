package se.sead.bugs;

import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.Row;
import com.healthmarketscience.jackcess.Table;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class TableReader {

    private Database database;

    public TableReader(Database database) {
        this.database = database;
    }

    public <T> List<T> read(BugsTable<T> bugsTable){
        try {
            Table table = database.getTable(bugsTable.getTableName());
            return read(table, bugsTable);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read table", e);
        }
    }

    private <T> List<T> read(Table table, BugsTable<T> bugsTable) {
        List<T> data = new ArrayList<>();
        for (Row dataRow : table) {
            data.add(bugsTable.createItem(dataRow));
        }
        return data;
    }

}
