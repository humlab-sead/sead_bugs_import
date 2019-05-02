package se.sead.bugsimport.speciesbiology.bugsmodel;

import se.sead.bugs.BugsColumn;
import se.sead.bugs.TraceableBugsData;

public class Biology extends TraceableBugsData {

    @BugsColumn("CODE")
    private Double code;
    @BugsColumn("Ref")
    private String ref;
    @BugsColumn("Data")
    private String data;

    public Double getCode() {
        return code;
    }

    public void setCode(Double code) {
        this.code = code;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String compressToString() {
        return "{" +
                code + "," +
                ref + "," +
                data + "}";
    }

    @Override
    public String bugsTable() {
        return BiologyBugsTable.TABLE_NAME;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Biology biology = (Biology) o;

        if (code != null ? !code.equals(biology.code) : biology.code != null) return false;
        if (ref != null ? !ref.equals(biology.ref) : biology.ref != null) return false;
        return data != null ? data.equals(biology.data) : biology.data == null;
    }

    @Override
    public int hashCode() {
        int result = code != null ? code.hashCode() : 0;
        result = 31 * result + (ref != null ? ref.hashCode() : 0);
        result = 31 * result + (data != null ? data.hashCode() : 0);
        return result;
    }
}
