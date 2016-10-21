package se.sead.bugsimport.speciesassociation.bugsmodel;

import se.sead.bugs.TraceableBugsData;

public class BugsSpeciesAssociation extends TraceableBugsData {

    private Integer speciesAssociationID;
    private Double code;
    private Double associatedSpeciesCODE;
    private String associationType;
    private String ref;

    public Integer getSpeciesAssociationID() {
        return speciesAssociationID;
    }

    public void setSpeciesAssociationID(Integer speciesAssociationID) {
        this.speciesAssociationID = speciesAssociationID;
    }

    public Double getCode() {
        return code;
    }

    public void setCode(Double code) {
        this.code = code;
    }

    public Double getAssociatedSpeciesCODE() {
        return associatedSpeciesCODE;
    }

    public void setAssociatedSpeciesCODE(Double associatedSpeciesCODE) {
        this.associatedSpeciesCODE = associatedSpeciesCODE;
    }

    public String getAssociationType() {
        return associationType;
    }

    public void setAssociationType(String associationType) {
        this.associationType = associationType;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    @Override
    public String compressToString() {
        return "{" +
                speciesAssociationID + ',' +
                code + ',' +
                associatedSpeciesCODE + ',' +
                associationType + ',' +
                ref + '}';
    }

    @Override
    public String bugsTable() {
        return SpeciesAssociationBugsTable.TABLE_NAME;
    }

    @Override
    public String getBugsIdentifier() {
        return String.valueOf(speciesAssociationID);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BugsSpeciesAssociation that = (BugsSpeciesAssociation) o;

        if (speciesAssociationID != null ? !speciesAssociationID.equals(that.speciesAssociationID) : that.speciesAssociationID != null)
            return false;
        if (code != null ? !code.equals(that.code) : that.code != null) return false;
        if (associatedSpeciesCODE != null ? !associatedSpeciesCODE.equals(that.associatedSpeciesCODE) : that.associatedSpeciesCODE != null)
            return false;
        if (associationType != null ? !associationType.equals(that.associationType) : that.associationType != null)
            return false;
        return ref != null ? ref.equals(that.ref) : that.ref == null;

    }

    @Override
    public int hashCode() {
        int result = speciesAssociationID != null ? speciesAssociationID.hashCode() : 0;
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (associatedSpeciesCODE != null ? associatedSpeciesCODE.hashCode() : 0);
        result = 31 * result + (associationType != null ? associationType.hashCode() : 0);
        result = 31 * result + (ref != null ? ref.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BugsSpeciesAssociation{" +
                "speciesAssociationID=" + speciesAssociationID +
                ", code=" + code +
                ", associatedSpeciesCODE=" + associatedSpeciesCODE +
                ", associationType='" + associationType + '\'' +
                ", ref='" + ref + '\'' +
                '}';
    }
}
