package se.sead.bugs;

import com.healthmarketscience.jackcess.Cursor;
import com.healthmarketscience.jackcess.CursorBuilder;
import com.healthmarketscience.jackcess.Row;
import com.healthmarketscience.jackcess.Table;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AccessSearcher<BugsType extends TraceableBugsData> {

    private AccessDatabase accessDatabase;
    private BugsTable<BugsType> bugsTable;

    public AccessSearcher(AccessDatabase accessDatabase, BugsTable<BugsType> bugsTable) {
        this.accessDatabase = accessDatabase;
        this.bugsTable = bugsTable;
    }

    public <ColumnType> Optional<BugsType> search(SearchCriteria<ColumnType> columnCriteria){
        try {
            Table table = getTable();
            Row row = findRow(columnCriteria, table);
            if(row != null){
                return Optional.of(getRowValue(row));
            } else {
                return Optional.empty();
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private Table getTable() throws IOException {
        return accessDatabase.getAccessDatabase().getTable(bugsTable.getTableName());
    }

    private <ColumnType> Row findRow(SearchCriteria<ColumnType> columnCriteria, Table table) throws IOException {
        Cursor cursor = CursorBuilder.createCursor(table);
        Map<String, ColumnType> columnCriteriaMap = createColumnCriteriaMap(columnCriteria);
        boolean firstRow = cursor.findFirstRow(columnCriteriaMap);
        Row result;
        if(firstRow){
            result = cursor.getCurrentRow();
            if(cursor.findNextRow(columnCriteriaMap)){
                throw new MultipleResultException();
            }
            return result;
        } else {
            return null;
        }
    }

    private <ColumnType> Map<String, ColumnType> createColumnCriteriaMap(SearchCriteria<ColumnType> columnCriteria) {
        Map<String, ColumnType> criteria = new HashMap<>();
        criteria.put(columnCriteria.getColumnName(), columnCriteria.getSearchValue());
        return criteria;
    }

    private BugsType getRowValue(Row row) {
        return  bugsTable.createItem(row);
    }

    public static class SearchCriteria<ColumnType> {
        private String columnName;
        private ColumnType searchValue;

        public SearchCriteria(
                String columnName,
                ColumnType searchValue) {
            this.columnName = columnName;
            this.searchValue = searchValue;
        }

        public String getColumnName() {
            return columnName;
        }

        public ColumnType getSearchValue() {
            return searchValue;
        }
    }

    public static class MultipleResultException extends UnsupportedOperationException {

    }
}
