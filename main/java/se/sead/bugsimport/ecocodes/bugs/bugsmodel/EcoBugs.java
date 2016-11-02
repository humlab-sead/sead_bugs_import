package se.sead.bugsimport.ecocodes.bugs.bugsmodel;

import se.sead.bugs.TraceableBugsData;

public class EcoBugs extends TraceableBugsData {

    private Double CODE;
    private String bugsEcoCODE;

    public Double getCODE() {
        return CODE;
    }

    public void setCODE(Double CODE) {
        this.CODE = CODE;
    }

    public String getBugsEcoCODE() {
        return bugsEcoCODE;
    }

    public void setBugsEcoCODE(String bugsEcoCODE) {
        this.bugsEcoCODE = bugsEcoCODE;
    }

    @Override
    public String compressToString() {
        return "{" +
                CODE + ',' +
                bugsEcoCODE + '}';
    }

    @Override
    public String bugsTable() {
        return EcoBugsBugsTable.TABLE_NAME;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EcoBugs ecoBugs = (EcoBugs) o;

        if (CODE != null ? !CODE.equals(ecoBugs.CODE) : ecoBugs.CODE != null) return false;
        return bugsEcoCODE != null ? bugsEcoCODE.equals(ecoBugs.bugsEcoCODE) : ecoBugs.bugsEcoCODE == null;

    }

    @Override
    public int hashCode() {
        int result = CODE != null ? CODE.hashCode() : 0;
        result = 31 * result + (bugsEcoCODE != null ? bugsEcoCODE.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "EcoBugs{" +
                "CODE=" + CODE +
                ", bugsEcoCODE='" + bugsEcoCODE + '\'' +
                '}';
    }
}
