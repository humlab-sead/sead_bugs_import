package se.sead.bugsimport.datesradio.bugsmodel;

import se.sead.bugs.BugsColumn;
import se.sead.bugs.TraceableBugsData;

public class DatesRadio extends TraceableBugsData {

    @BugsColumn("DateCODE")
    private String dateCode;
    @BugsColumn("SampleCODE")
    private String sampleCode;
    @BugsColumn("LabNr")
    private String labNr;
    @BugsColumn("Uncertainty")
    private String uncertainty;
    @BugsColumn("Date")
    private Integer date;
    @BugsColumn("AgeErrorOrPlusError")
    private Short ageErrorOrPlusError;
    @BugsColumn("AgeErrorMinus")
    private Integer ageErrorMinus;
    @BugsColumn("DatingMethod")
    private String datingMethod;
    @BugsColumn("MaterialType")
    private String materialType;
    @BugsColumn("LabID")
    private String labId;
    @BugsColumn("Notes")
    private String notes;

    public String getDateCode() {
        return dateCode;
    }

    public void setDateCode(String dateCode) {
        this.dateCode = dateCode;
    }

    public String getSampleCode() {
        return sampleCode;
    }

    public void setSampleCode(String sampleCode) {
        this.sampleCode = sampleCode;
    }

    public String getLabNr() {
        return labNr;
    }

    public void setLabNr(String labNr) {
        this.labNr = labNr;
    }

    public String getUncertainty() {
        return uncertainty;
    }

    public void setUncertainty(String uncertainty) {
        this.uncertainty = uncertainty;
    }

    public Integer getDate() {
        return date;
    }

    public void setDate(Integer date) {
        this.date = date;
    }

    public Short getAgeErrorOrPlusError() {
        return ageErrorOrPlusError;
    }

    public void setAgeErrorOrPlusError(Short ageErrorOrPlusError) {
        this.ageErrorOrPlusError = ageErrorOrPlusError;
    }

    public Integer getAgeErrorMinus() {
        return ageErrorMinus;
    }

    public void setAgeErrorMinus(Integer ageErrorMinus) {
        this.ageErrorMinus = ageErrorMinus;
    }

    public String getDatingMethod() {
        return datingMethod;
    }

    public void setDatingMethod(String datingMethod) {
        this.datingMethod = datingMethod;
    }

    public String getMaterialType() {
        return materialType;
    }

    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }

    public String getLabId() {
        return labId;
    }

    public void setLabId(String labId) {
        this.labId = labId;
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
                dateCode + ',' +
                sampleCode + ',' +
                labNr + ',' +
                uncertainty + ',' +
                date + ',' +
                ageErrorOrPlusError + ',' +
                ageErrorMinus + ',' +
                datingMethod + ',' +
                materialType + ',' +
                labId + ',' +
                notes +
                '}';
    }

    @Override
    public String bugsTable() {
        return DatesRadioBugsTable.TABLE_NAME;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DatesRadio that = (DatesRadio) o;

        if (dateCode != null ? !dateCode.equals(that.dateCode) : that.dateCode != null) return false;
        if (sampleCode != null ? !sampleCode.equals(that.sampleCode) : that.sampleCode != null) return false;
        if (labNr != null ? !labNr.equals(that.labNr) : that.labNr != null) return false;
        if (uncertainty != null ? !uncertainty.equals(that.uncertainty) : that.uncertainty != null) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        if (ageErrorOrPlusError != null ? !ageErrorOrPlusError.equals(that.ageErrorOrPlusError) : that.ageErrorOrPlusError != null)
            return false;
        if (ageErrorMinus != null ? !ageErrorMinus.equals(that.ageErrorMinus) : that.ageErrorMinus != null)
            return false;
        if (datingMethod != null ? !datingMethod.equals(that.datingMethod) : that.datingMethod != null) return false;
        if (materialType != null ? !materialType.equals(that.materialType) : that.materialType != null) return false;
        if (labId != null ? !labId.equals(that.labId) : that.labId != null) return false;
        return notes != null ? notes.equals(that.notes) : that.notes == null;

    }

    @Override
    public int hashCode() {
        int result = dateCode != null ? dateCode.hashCode() : 0;
        result = 31 * result + (sampleCode != null ? sampleCode.hashCode() : 0);
        result = 31 * result + (labNr != null ? labNr.hashCode() : 0);
        result = 31 * result + (uncertainty != null ? uncertainty.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (ageErrorOrPlusError != null ? ageErrorOrPlusError.hashCode() : 0);
        result = 31 * result + (ageErrorMinus != null ? ageErrorMinus.hashCode() : 0);
        result = 31 * result + (datingMethod != null ? datingMethod.hashCode() : 0);
        result = 31 * result + (materialType != null ? materialType.hashCode() : 0);
        result = 31 * result + (labId != null ? labId.hashCode() : 0);
        result = 31 * result + (notes != null ? notes.hashCode() : 0);
        return result;
    }

    @Override
    public String getBugsIdentifier() {
        return dateCode;
    }

    @Override
    public String toString() {
        return "DatesRadio{" +
                "dateCode='" + dateCode + '\'' +
                ", sampleCode='" + sampleCode + '\'' +
                ", labNr='" + labNr + '\'' +
                ", uncertainty='" + uncertainty + '\'' +
                ", date=" + date +
                ", ageErrorOrPlusError=" + ageErrorOrPlusError +
                ", ageErrorMinus=" + ageErrorMinus +
                ", datingMethod='" + datingMethod + '\'' +
                ", materialType='" + materialType + '\'' +
                ", labId='" + labId + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }
}
