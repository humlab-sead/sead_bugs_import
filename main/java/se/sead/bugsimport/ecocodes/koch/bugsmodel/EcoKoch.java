package se.sead.bugsimport.ecocodes.koch.bugsmodel;

import se.sead.bugs.TraceableBugsData;

public class EcoKoch extends TraceableBugsData {

    private Double CODE;
    private String bugsKochCode;

    public Double getCODE() {
        return CODE;
    }

    public void setCODE(Double CODE) {
        this.CODE = CODE;
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
                CODE + ',' +
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

        if (CODE != null ? !CODE.equals(ecoKoch.CODE) : ecoKoch.CODE != null) return false;
        return bugsKochCode != null ? bugsKochCode.equals(ecoKoch.bugsKochCode) : ecoKoch.bugsKochCode == null;

    }

    @Override
    public int hashCode() {
        int result = CODE != null ? CODE.hashCode() : 0;
        result = 31 * result + (bugsKochCode != null ? bugsKochCode.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "EcoKoch{" +
                "CODE=" + CODE +
                ", bugsKochCode='" + bugsKochCode + '\'' +
                '}';
    }
}
