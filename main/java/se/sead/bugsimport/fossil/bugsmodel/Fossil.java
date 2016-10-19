package se.sead.bugsimport.fossil.bugsmodel;

import se.sead.bugs.TraceableBugsData;

public class Fossil extends TraceableBugsData {

    private String fossilBugsCODE;
    private Double code;
    private String sampleCODE;
    private Integer abundance;

    public String getFossilBugsCODE() {
        return fossilBugsCODE;
    }

    public void setFossilBugsCODE(String fossilBugsCODE) {
        this.fossilBugsCODE = fossilBugsCODE;
    }

    public Double getCode() {
        return code;
    }

    public void setCode(Double code) {
        this.code = code;
    }

    public String getSampleCODE() {
        return sampleCODE;
    }

    public void setSampleCODE(String sampleCODE) {
        this.sampleCODE = sampleCODE;
    }

    public Integer getAbundance() {
        return abundance;
    }

    public void setAbundance(Integer abundance) {
        this.abundance = abundance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Fossil fossil = (Fossil) o;

        if (fossilBugsCODE != null ? !fossilBugsCODE.equals(fossil.fossilBugsCODE) : fossil.fossilBugsCODE != null)
            return false;
        if (code != null ? !code.equals(fossil.code) : fossil.code != null) return false;
        if (sampleCODE != null ? !sampleCODE.equals(fossil.sampleCODE) : fossil.sampleCODE != null) return false;
        return abundance != null ? abundance.equals(fossil.abundance) : fossil.abundance == null;

    }

    @Override
    public int hashCode() {
        int result = fossilBugsCODE != null ? fossilBugsCODE.hashCode() : 0;
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (sampleCODE != null ? sampleCODE.hashCode() : 0);
        result = 31 * result + (abundance != null ? abundance.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Fossil{" +
                "fossilBugsCODE='" + fossilBugsCODE + '\'' +
                ", code=" + code +
                ", sampleCODE='" + sampleCODE + '\'' +
                ", abundance=" + abundance +
                '}';
    }

    @Override
    public String compressToString() {
        return "{" +
                fossilBugsCODE + ',' +
                code + ',' +
                sampleCODE + ',' +
                abundance + '}';
    }

    @Override
    public String bugsTable() {
        return FossilBugsTable.TABLE_NAME;
    }

    @Override
    public String getBugsIdentifier() {
        return fossilBugsCODE;
    }
}
