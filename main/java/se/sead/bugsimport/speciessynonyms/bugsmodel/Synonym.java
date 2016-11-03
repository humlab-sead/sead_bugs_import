package se.sead.bugsimport.speciessynonyms.bugsmodel;

import se.sead.bugs.TraceableBugsData;

public class Synonym extends TraceableBugsData {

    private Double CODE;
    private String synGenus;
    private String synSpecies;
    private String synAuthority;
    private String reference;
    private String notes;

    public Double getCODE() {
        return CODE;
    }

    public void setCODE(Double CODE) {
        this.CODE = CODE;
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
                CODE + ',' +
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

        if (CODE != null ? !CODE.equals(synonym.CODE) : synonym.CODE != null) return false;
        if (synGenus != null ? !synGenus.equals(synonym.synGenus) : synonym.synGenus != null) return false;
        if (synSpecies != null ? !synSpecies.equals(synonym.synSpecies) : synonym.synSpecies != null) return false;
        if (synAuthority != null ? !synAuthority.equals(synonym.synAuthority) : synonym.synAuthority != null)
            return false;
        if (reference != null ? !reference.equals(synonym.reference) : synonym.reference != null) return false;
        return notes != null ? notes.equals(synonym.notes) : synonym.notes == null;

    }

    @Override
    public int hashCode() {
        int result = CODE != null ? CODE.hashCode() : 0;
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
                "CODE=" + CODE +
                ", synGenus='" + synGenus + '\'' +
                ", synSpecies='" + synSpecies + '\'' +
                ", synAuthority='" + synAuthority + '\'' +
                ", reference='" + reference + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }
}
