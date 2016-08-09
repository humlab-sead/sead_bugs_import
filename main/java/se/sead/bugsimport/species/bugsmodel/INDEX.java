package se.sead.bugsimport.species.bugsmodel;

import se.sead.bugs.TraceableBugsData;

/**
 * Created by erer0001 on 2016-04-28.
 */
public class INDEX extends TraceableBugsData {
    private Double code;
    private String family;
    private String genus;
    private String species;
    private String authority;

    public Double getCode() {
        return code;
    }

    public void setCode(Double code) {
        this.code = code;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getGenus() {
        return genus;
    }

    public void setGenus(String genus) {
        this.genus = genus;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        INDEX index = (INDEX) o;

        if (!code.equals(index.code)) return false;
        if (family != null ? !family.equals(index.family) : index.family != null) return false;
        if (genus != null ? !genus.equals(index.genus) : index.genus != null) return false;
        if (species != null ? !species.equals(index.species) : index.species != null) return false;
        return authority != null ? authority.equals(index.authority) : index.authority == null;

    }

    @Override
    public int hashCode() {
        int result = code.hashCode();
        result = 31 * result + (family != null ? family.hashCode() : 0);
        result = 31 * result + (genus != null ? genus.hashCode() : 0);
        result = 31 * result + (species != null ? species.hashCode() : 0);
        result = 31 * result + (authority != null ? authority.hashCode() : 0);
        return result;
    }

    @Override
    public String compressToString() {
        return "{" +
                code + "," +
                family + "," +
                genus + "," +
                species +
                (authority != null ? "," + authority : "") +
                "}";
    }

    @Override
    public String bugsTable() {
        return INDEXBugsTable.TABLE_NAME;
    }
}
