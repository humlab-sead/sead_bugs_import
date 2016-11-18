package se.sead.bugsimport.ecocodes.koch.bugsmodel;

import se.sead.bugs.BugsColumn;
import se.sead.bugs.TraceableBugsData;

public class EcoKoch extends TraceableBugsData {

    @BugsColumn("CODE")
    private Double code;
    @BugsColumn("BugsKochCode")
    private String bugsKochCode;

    public Double getCode() {
        return code;
    }

    public void setCode(Double code) {
        this.code = code;
    }

    public String getBugsKochCode() {
        return bugsKochCode;
    }

    public void setBugsKochCode(String bugsKochCode) {
        this.bugsKochCode = bugsKochCode;
    }

    @Override
    public String compressToString() {
        return "{" +
                code + ',' +
                bugsKochCode +
                '}';
    }

    @Override
    public String bugsTable() {
        return EcoKochBugsTable.TABLE_NAME;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EcoKoch ecoKoch = (EcoKoch) o;

        if (code != null ? !code.equals(ecoKoch.code) : ecoKoch.code != null) return false;
        return bugsKochCode != null ? bugsKochCode.equals(ecoKoch.bugsKochCode) : ecoKoch.bugsKochCode == null;

    }

    @Override
    public int hashCode() {
        int result = code != null ? code.hashCode() : 0;
        result = 31 * result + (bugsKochCode != null ? bugsKochCode.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "EcoKoch{" +
                "code=" + code +
                ", bugsKochCode='" + bugsKochCode + '\'' +
                '}';
    }
}
