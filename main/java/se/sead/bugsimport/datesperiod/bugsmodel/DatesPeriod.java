package se.sead.bugsimport.datesperiod.bugsmodel;

import se.sead.bugs.TraceableBugsData;

public class DatesPeriod extends TraceableBugsData {

    private String periodDateCode;
    private String sampleCode;
    private String uncertainty;
    private String periodCode;
    private String datingMethod;
    private String notes;

    public String getPeriodDateCode() {
        return periodDateCode;
    }

    public void setPeriodDateCode(String periodDateCode) {
        this.periodDateCode = periodDateCode;
    }

    public String getSampleCode() {
        return sampleCode;
    }

    public void setSampleCode(String sampleCode) {
        this.sampleCode = sampleCode;
    }

    public String getUncertainty() {
        return uncertainty;
    }

    public void setUncertainty(String uncertainty) {
        this.uncertainty = uncertainty;
    }

    public String getPeriodCode() {
        return periodCode;
    }

    public void setPeriodCode(String periodCode) {
        this.periodCode = periodCode;
    }

    public String getDatingMethod() {
        return datingMethod;
    }

    public void setDatingMethod(String datingMethod) {
        this.datingMethod = datingMethod;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String compressToString() {
        return "{" +
                periodDateCode + ',' +
                sampleCode + ',' +
                uncertainty + ',' +
                periodCode + ',' +
                datingMethod + ',' +
                notes + '}';
    }

    @Override
    public String bugsTable() {
        return DatesPeriodBugsTable.TABLE_NAME;
    }

    @Override
    public String getBugsIdentifier() {
        return periodDateCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DatesPeriod that = (DatesPeriod) o;

        if (periodDateCode != null ? !periodDateCode.equals(that.periodDateCode) : that.periodDateCode != null)
            return false;
        if (sampleCode != null ? !sampleCode.equals(that.sampleCode) : that.sampleCode != null) return false;
        if (uncertainty != null ? !uncertainty.equals(that.uncertainty) : that.uncertainty != null) return false;
        if (periodCode != null ? !periodCode.equals(that.periodCode) : that.periodCode != null) return false;
        if (datingMethod != null ? !datingMethod.equals(that.datingMethod) : that.datingMethod != null) return false;
        return notes != null ? notes.equals(that.notes) : that.notes == null;

    }

    @Override
    public String toString() {
        return "DatesPeriod{" +
                "periodDateCode='" + periodDateCode + '\'' +
                ", sampleCode='" + sampleCode + '\'' +
                ", uncertainty='" + uncertainty + '\'' +
                ", periodCode='" + periodCode + '\'' +
                ", datingMethod='" + datingMethod + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        int result = periodDateCode != null ? periodDateCode.hashCode() : 0;
        result = 31 * result + (sampleCode != null ? sampleCode.hashCode() : 0);
        result = 31 * result + (uncertainty != null ? uncertainty.hashCode() : 0);
        result = 31 * result + (periodCode != null ? periodCode.hashCode() : 0);
        result = 31 * result + (datingMethod != null ? datingMethod.hashCode() : 0);
        result = 31 * result + (notes != null ? notes.hashCode() : 0);
        return result;
    }
}
