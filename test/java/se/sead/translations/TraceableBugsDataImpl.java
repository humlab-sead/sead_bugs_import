package se.sead.translations;

import se.sead.bugs.TraceableBugsData;

import java.math.BigDecimal;

public class TraceableBugsDataImpl extends TraceableBugsData {

    public static final String TEST_IMPLEMENTATION_BUGS_TABLE_NAME = "TestImplementation";

    private Integer id;
    private String columnValue;
    private BigDecimal numericValue;

    private boolean exportIdentifierMethod;

    TraceableBugsDataImpl(){this(false);}
    TraceableBugsDataImpl(boolean exportIdentifierMethod){
        this.exportIdentifierMethod = exportIdentifierMethod;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getColumnValue() {
        return columnValue;
    }

    public void setColumnValue(String columnValue) {
        this.columnValue = columnValue;
    }

    public BigDecimal getNumericValue() {
        return numericValue;
    }

    public void setNumericValue(BigDecimal numericValue) {
        this.numericValue = numericValue;
    }

    @Override
    public String compressToString() {
        return "{" +
                getId() + "," +
                getColumnValue() + "," +
                getNumericValue() +
                "}";
    }

    @Override
    public String getBugsIdentifier() {
        if(exportIdentifierMethod){
            return Integer.toString(id);
        } else {
            return super.getBugsIdentifier();
        }
    }

    @Override
    public String bugsTable() {
        return TEST_IMPLEMENTATION_BUGS_TABLE_NAME;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof TraceableBugsDataImpl)) return false;

        TraceableBugsDataImpl that = (TraceableBugsDataImpl) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (columnValue != null ? !columnValue.equals(that.columnValue) : that.columnValue != null) return false;
        return numericValue != null ? numericValue.equals(that.numericValue) : that.numericValue == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (columnValue != null ? columnValue.hashCode() : 0);
        result = 31 * result + (numericValue != null ? numericValue.hashCode() : 0);
        return result;
    }
}
