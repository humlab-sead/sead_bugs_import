package se.sead.bugsimport.speciessynonyms.bugsmodel;

import se.sead.bugs.BugsColumn;
import se.sead.bugs.TraceableBugsData;

public class Synonym extends TraceableBugsData {

    @BugsColumn("CODE")
    private Double code;
    @BugsColumn("SynGenus")
    private String synGenus;
    @BugsColumn("SynSpecies")
    private String synSpecies;
    @BugsColumn("SynAuthority")
    private String synAuthority;
    @BugsColumn("Ref")
    private String reference;
    @BugsColumn("Notes")
    private String notes;

    public Double getCode() {
        return code;
    }

    public void setCode(Double code) {
        this.code = code;
    }

    public String getSynGenus() {
        return synGenus;
    }

    public void setSynGenus(String synGenus) {
        this.synGenus = synGenus;
    }

    public String getSynSpecies() {
        return synSpecies;
    }

    public void setSynSpecies(String synSpecies) {
        this.synSpecies = synSpecies;
    }

    public String getSynAuthority() {
        return synAuthority;
    }

    public void setSynAuthority(String synAuthority) {
        this.synAuthority = synAuthority;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
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
                code + ',' +
                synGenus + ',' +
                synSpecies + ',' +
                synAuthority + ',' +
                reference + ',' +
                notes + '}';
    }

    @Override
    public String bugsTable() {
        return SynonymBugsTable.TABLE_NAME;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Synonym synonym = (Synonym) o;

        if (code != null ? !code.equals(synonym.code) : synonym.code != null) return false;
        if (synGenus != null ? !synGenus.equals(synonym.synGenus) : synonym.synGenus != null) return false;
        if (synSpecies != null ? !synSpecies.equals(synonym.synSpecies) : synonym.synSpecies != null) return false;
        if (synAuthority != null ? !synAuthority.equals(synonym.synAuthority) : synonym.synAuthority != null)
            return false;
        if (reference != null ? !reference.equals(synonym.reference) : synonym.reference != null) return false;
        return notes != null ? notes.equals(synonym.notes) : synonym.notes == null;

    }

    @Override
    public int hashCode() {
        int result = code != null ? code.hashCode() : 0;
        result = 31 * result + (synGenus != null ? synGenus.hashCode() : 0);
        result = 31 * result + (synSpecies != null ? synSpecies.hashCode() : 0);
        result = 31 * result + (synAuthority != null ? synAuthority.hashCode() : 0);
        result = 31 * result + (reference != null ? reference.hashCode() : 0);
        result = 31 * result + (notes != null ? notes.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Synonym{" +
                "code=" + code +
                ", synGenus='" + synGenus + '\'' +
                ", synSpecies='" + synSpecies + '\'' +
                ", synAuthority='" + synAuthority + '\'' +
                ", reference='" + reference + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }
}
