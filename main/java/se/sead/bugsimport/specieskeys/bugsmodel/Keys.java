package se.sead.bugsimport.specieskeys.bugsmodel;

import se.sead.bugs.TraceableBugsData;

public class Keys implements TraceableBugsData {

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
        return KeysBugsTable.TABLE_NAME;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Keys keys = (Keys) o;

        if (!code.equals(keys.code)) return false;
        if (ref != null ? !ref.equals(keys.ref) : keys.ref != null) return false;
        return data != null ? data.equals(keys.data) : keys.data == null;

    }

    @Override
    public int hashCode() {
        int result = code.hashCode();
        result = 31 * result + (ref != null ? ref.hashCode() : 0);
        result = 31 * result + (data != null ? data.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Keys{" +
                "code=" + code +
                ", ref='" + ref + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
