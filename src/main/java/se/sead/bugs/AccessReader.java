package se.sead.bugs;

import com.healthmarketscience.jackcess.Row;
import com.healthmarketscience.jackcess.Table;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AccessReader {

    private AccessDatabase database;

    public AccessReader(AccessDatabase database) {
        this.database = database;
    }

    public <T> List<T> read(BugsTable<T> bugsTable){
        try {
            Table table = getTableFromBugsDefinition(bugsTable);
            return read(table, bugsTable);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read table", e);
        }
    }

    private <T> Table getTableFromBugsDefinition(BugsTable<T> bugsTable) throws IOException {
        return database.getAccessDatabase().getTable(bugsTable.getTableName());
    }

    private <T> List<T> read(Table table, BugsTable<T> bugsTable) {
        List<T> data = new ArrayList<>();
        for (Row dataRow : table) {
            data.add(bugsTable.createItem(dataRow));
        }
        return data;
    }

}
