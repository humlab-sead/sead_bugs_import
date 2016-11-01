package se.sead.bugsimport.ecocodedefinition.bugs.bugsmodel;

import se.sead.bugs.TraceableBugsData;

public class EcoDefBugs extends TraceableBugsData {

    private String bugsEcoCODE;
    private String definition;
    private String notes;
    private String ecoLabel;
    private Short sortOrder;

    public String getBugsEcoCODE() {
        return bugsEcoCODE;
    }

    public void setBugsEcoCODE(String bugsEcoCODE) {
        this.bugsEcoCODE = bugsEcoCODE;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getEcoLabel() {
        return ecoLabel;
    }

    public void setEcoLabel(String ecoLabel) {
        this.ecoLabel = ecoLabel;
    }

    public Short getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Short sortOrder) {
        this.sortOrder = sortOrder;
    }

    @Override
    public String compressToString() {
        return "{" +
                bugsEcoCODE + ',' +
                definition + ',' +
                notes + ',' +
                ecoLabel + ',' +
                sortOrder + '}';
    }

    @Override
    public String bugsTable() {
        return EcoDefBugsBugsTable.TABLE_NAME;
    }

    @Override
    public String getBugsIdentifier() {
        return bugsEcoCODE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EcoDefBugs that = (EcoDefBugs) o;

        if (bugsEcoCODE != null ? !bugsEcoCODE.equals(that.bugsEcoCODE) : that.bugsEcoCODE != null) return false;
        if (definition != null ? !definition.equals(that.definition) : that.definition != null) return false;
        if (notes != null ? !notes.equals(that.notes) : that.notes != null) return false;
        if (ecoLabel != null ? !ecoLabel.equals(that.ecoLabel) : that.ecoLabel != null) return false;
        return sortOrder != null ? sortOrder.equals(that.sortOrder) : that.sortOrder == null;

    }

    @Override
    public int hashCode() {
        int result = bugsEcoCODE != null ? bugsEcoCODE.hashCode() : 0;
        result = 31 * result + (definition != null ? definition.hashCode() : 0);
        result = 31 * result + (notes != null ? notes.hashCode() : 0);
        result = 31 * result + (ecoLabel != null ? ecoLabel.hashCode() : 0);
        result = 31 * result + (sortOrder != null ? sortOrder.hashCode() : 0);
        return result;
    }
}
