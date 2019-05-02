package se.sead.bugsimport.ecocodes.bugs.bugsmodel;

import se.sead.bugs.BugsColumn;
import se.sead.bugs.TraceableBugsData;

public class EcoBugs extends TraceableBugsData {

    @BugsColumn("CODE")
    private Double code;
    @BugsColumn("BugsEcoCODE")
    private String bugsEcoCode;

    public Double getCode() {
        return code;
    }

    public void setCode(Double code) {
        this.code = code;
    }

    public String getBugsEcoCode() {
        return bugsEcoCode;
    }

    public void setBugsEcoCode(String bugsEcoCode) {
        this.bugsEcoCode = bugsEcoCode;
    }

    @Override
    public String compressToString() {
        return "{" +
                code + ',' +
                bugsEcoCode + '}';
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

        if (code != null ? !code.equals(ecoBugs.code) : ecoBugs.code != null) return false;
        return bugsEcoCode != null ? bugsEcoCode.equals(ecoBugs.bugsEcoCode) : ecoBugs.bugsEcoCode == null;

    }

    @Override
    public int hashCode() {
        int result = code != null ? code.hashCode() : 0;
        result = 31 * result + (bugsEcoCode != null ? bugsEcoCode.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "EcoBugs{" +
                "code=" + code +
                ", bugsEcoCode='" + bugsEcoCode + '\'' +
                '}';
    }
}
