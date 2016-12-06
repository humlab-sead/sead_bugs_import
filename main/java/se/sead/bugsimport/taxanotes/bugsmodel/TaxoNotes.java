package se.sead.bugsimport.taxanotes.bugsmodel;

import se.sead.bugs.BugsColumn;
import se.sead.bugs.TraceableBugsData;

public class TaxoNotes extends TraceableBugsData {

    @BugsColumn("CODE")
    private Double code;
    @BugsColumn("Ref")
    private String reference;
    @BugsColumn("Data")
    private String data;

    public Double getCode() {
        return code;
    }

    public void setCode(Double code) {
        this.code = code;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String compressToString() {
        return "{"
                + code + ','
                + reference + ','
                + data + '}';
    }

    @Override
    public String bugsTable() {
        return TaxoNotesBugsTable.TABLE_NAME;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TaxoNotes taxoNotes = (TaxoNotes) o;

        if (code != null ? !code.equals(taxoNotes.code) : taxoNotes.code != null) return false;
        if (reference != null ? !reference.equals(taxoNotes.reference) : taxoNotes.reference != null) return false;
        return data != null ? data.equals(taxoNotes.data) : taxoNotes.data == null;
    }

    @Override
    public int hashCode() {
        int result = code != null ? code.hashCode() : 0;
        result = 31 * result + (reference != null ? reference.hashCode() : 0);
        result = 31 * result + (data != null ? data.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TaxoNotes{" +
                "code=" + code +
                ", reference='" + reference + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
