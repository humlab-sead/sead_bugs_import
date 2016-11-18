package se.sead.bugsimport.ecocodedefinition.koch.bugsmodel;

import se.sead.bugs.BugsColumn;
import se.sead.bugs.TraceableBugsData;
import se.sead.bugsimport.ecocodedefinitiongroups.bugsmodel.EcoDefGroupsBugsTable;

public class EcoDefKoch extends TraceableBugsData{

    @BugsColumn("BugsKochCode")
    private String bugsKochCode;
    @BugsColumn("KochCode")
    private String kochCode;
    @BugsColumn("FullName")
    private String fullName;
    @BugsColumn("KochGroup")
    private String kochGroup;
    @BugsColumn("Description")
    private String description;
    @BugsColumn("Notes")
    private String notes;

    public String getBugsKochCode() {
        return bugsKochCode;
    }

    public void setBugsKochCode(String bugsKochCode) {
        this.bugsKochCode = bugsKochCode;
    }

    public String getKochCode() {
        return kochCode;
    }

    public void setKochCode(String kochCode) {
        this.kochCode = kochCode;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getKochGroup() {
        return kochGroup;
    }

    public void setKochGroup(String kochGroup) {
        this.kochGroup = kochGroup;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
                bugsKochCode + ',' +
                kochCode + ',' +
                fullName + ',' +
                kochGroup + ',' +
                description + ',' +
                notes + "}";
    }

    @Override
    public String getBugsIdentifier() {
        return bugsKochCode;
    }

    @Override
    public String bugsTable() {
        return EcoDefKochBugsTable.TABLE_NAME;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EcoDefKoch that = (EcoDefKoch) o;

        if (!bugsKochCode.equals(that.bugsKochCode)) return false;
        if (kochCode != null ? !kochCode.equals(that.kochCode) : that.kochCode != null) return false;
        if (fullName != null ? !fullName.equals(that.fullName) : that.fullName != null) return false;
        if (kochGroup != null ? !kochGroup.equals(that.kochGroup) : that.kochGroup != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        return notes != null ? notes.equals(that.notes) : that.notes == null;

    }

    @Override
    public int hashCode() {
        int result = bugsKochCode.hashCode();
        result = 31 * result + (kochCode != null ? kochCode.hashCode() : 0);
        result = 31 * result + (fullName != null ? fullName.hashCode() : 0);
        result = 31 * result + (kochGroup != null ? kochGroup.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (notes != null ? notes.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "EcoDefKoch{" +
                "bugsKochCode='" + bugsKochCode + '\'' +
                ", kochCode='" + kochCode + '\'' +
                ", fullName='" + fullName + '\'' +
                ", kochGroup='" + kochGroup + '\'' +
                ", description='" + description + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }
}
