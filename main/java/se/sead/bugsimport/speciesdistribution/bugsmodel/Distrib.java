package se.sead.bugsimport.speciesdistribution.bugsmodel;

import se.sead.bugs.TraceableBugsData;

public class Distrib extends TraceableBugsData {

    private Double code;
    private String ref;
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
        return '{' +
                code + ',' +
                ref + ',' +
                data + '}';
    }

    @Override
    public String bugsTable() {
        return DistribBugsTable.TABLE_NAME;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Distrib distrib = (Distrib) o;

        if (code != null ? !code.equals(distrib.code) : distrib.code != null) return false;
        if (ref != null ? !ref.equals(distrib.ref) : distrib.ref != null) return false;
        return data != null ? data.equals(distrib.data) : distrib.data == null;

    }

    @Override
    public int hashCode() {
        int result = code != null ? code.hashCode() : 0;
        result = 31 * result + (ref != null ? ref.hashCode() : 0);
        result = 31 * result + (data != null ? data.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Distrib{" +
                "code=" + code +
                ", ref='" + ref + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
