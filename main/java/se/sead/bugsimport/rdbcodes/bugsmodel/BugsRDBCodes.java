package se.sead.bugsimport.rdbcodes.bugsmodel;

import se.sead.bugs.TraceableBugsData;

public class BugsRDBCodes extends TraceableBugsData {

    private Integer rdbCode;
    private String category;
    private String rdbDefinition;
    private Integer rdbSystemCode;

    public Integer getRdbCode() {
        return rdbCode;
    }

    public void setRdbCode(Integer rdbCode) {
        this.rdbCode = rdbCode;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRdbDefinition() {
        return rdbDefinition;
    }

    public void setRdbDefinition(String rdbDefinition) {
        this.rdbDefinition = rdbDefinition;
    }

    public Integer getRdbSystemCode() {
        return rdbSystemCode;
    }

    public void setRdbSystemCode(Integer rdbSystemCode) {
        this.rdbSystemCode = rdbSystemCode;
    }

    @Override
    public String compressToString() {
        return "{" +
                rdbCode + ',' +
                category + ',' +
                rdbDefinition + ',' +
                rdbSystemCode +
                '}';
    }

    @Override
    public String bugsTable() {
        return RDBCodesBugsTable.TABLE_NAME;
    }

    @Override
    public String getBugsIdentifier() {
        return String.valueOf(rdbCode);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BugsRDBCodes that = (BugsRDBCodes) o;

        if (rdbCode != null ? !rdbCode.equals(that.rdbCode) : that.rdbCode != null) return false;
        if (category != null ? !category.equals(that.category) : that.category != null) return false;
        if (rdbDefinition != null ? !rdbDefinition.equals(that.rdbDefinition) : that.rdbDefinition != null)
            return false;
        return rdbSystemCode != null ? rdbSystemCode.equals(that.rdbSystemCode) : that.rdbSystemCode == null;

    }

    @Override
    public int hashCode() {
        int result = rdbCode != null ? rdbCode.hashCode() : 0;
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (rdbDefinition != null ? rdbDefinition.hashCode() : 0);
        result = 31 * result + (rdbSystemCode != null ? rdbSystemCode.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BugsRDBCodes{" +
                "rdbCode=" + rdbCode +
                ", category='" + category + '\'' +
                ", rdbDefinition='" + rdbDefinition + '\'' +
                ", rdbSystemCode=" + rdbSystemCode +
                '}';
    }
}
