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

    public AccessSearcher(AccessDatabase accessDatabase) {
        this.accessDatabase = accessDatabase;
    }

    public <ColumnType> Optional<BugsType> search(BugsTable<BugsType> bugsTable, SearchCriteria<ColumnType> columnCriteria){
        try {
            Table table = getTable(bugsTable);
            Row row = findRow(columnCriteria, table);
            if(row != null){
                return Optional.of(getRowValue(row, bugsTable));
            } else {
                return Optional.empty();
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private Table getTable(BugsTable<BugsType> bugsTable) throws IOException {
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

    private BugsType getRowValue(Row row, BugsTable<BugsType> bugsTable) {
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
