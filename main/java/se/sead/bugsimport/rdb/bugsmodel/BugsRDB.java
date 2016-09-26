package se.sead.bugsimport.rdb.bugsmodel;

import se.sead.bugs.TraceableBugsData;

public class BugsRDB extends TraceableBugsData {

    private Double code;
    private String countryCode;
    private Integer rdbCode;

    public Double getCode() {
        return code;
    }

    public void setCode(Double code) {
        this.code = code;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public Integer getRdbCode() {
        return rdbCode;
    }

    public void setRdbCode(Integer rdbCode) {
        this.rdbCode = rdbCode;
    }

    @Override
    public String compressToString() {
        return "{" +
                (code == null ? "" : code.toString()) + ',' +
                countryCode + ',' +
                rdbCode
                + '}' ;
    }

    @Override
    public String getBugsIdentifier() {
        return code != null ? code.toString() : null;
    }

    @Override
    public String bugsTable() {
        return RDBBugsTable.TABLE_NAME;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BugsRDB bugsRDB = (BugsRDB) o;

        if (code != null ? !code.equals(bugsRDB.code) : bugsRDB.code != null) return false;
        if (countryCode != null ? !countryCode.equals(bugsRDB.countryCode) : bugsRDB.countryCode != null) return false;
        return rdbCode != null ? rdbCode.equals(bugsRDB.rdbCode) : bugsRDB.rdbCode == null;

    }

    @Override
    public int hashCode() {
        int result = code != null ? code.hashCode() : 0;
        result = 31 * result + (countryCode != null ? countryCode.hashCode() : 0);
        result = 31 * result + (rdbCode != null ? rdbCode.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BugsRDB{" +
                "code=" + code +
                ", countryCode='" + countryCode + '\'' +
                ", rdbCode=" + rdbCode +
                '}';
    }
}
