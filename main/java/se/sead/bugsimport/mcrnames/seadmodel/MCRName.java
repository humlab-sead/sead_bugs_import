package se.sead.bugsimport.mcrnames.seadmodel;

import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.sead.model.LoggableEntity;

import javax.persistence.*;

@Entity
@Table(name = "tbl_mcr_names")
@SequenceGenerator(name="mcrname_id_gen", sequenceName = "tbl_mcr_names_taxon_id_seq")
public class MCRName extends LoggableEntity {

    @Id
    @GeneratedValue(generator = "mcrname_id_gen", strategy = GenerationType.IDENTITY)
    @Column(name="taxon_id", nullable = false)
    private Integer id;
    @Column(name="comparison_notes")
    private String notes;
    @Column(name="mcr_name_trim")
    private String shortName;
    @Column(name="mcr_number")
    private Short mcrNumber;
    @Column(name="mcr_species_name")
    private String mcrName;

    @Override
    public Integer getId() {
        return id;
    }

    protected void setId(Integer id) {
        this.id = id;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Short getMcrNumber() {
        return mcrNumber;
    }

    public void setMcrNumber(Short mcrNumber) {
        this.mcrNumber = mcrNumber;
    }

    public String getMcrName() {
        return mcrName;
    }

    public void setMcrName(String mcrName) {
        this.mcrName = mcrName;
    }

    public void setSpecies(TaxaSpecies species){
        setId(species.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof MCRName)) return false;

        MCRName mcrName1 = (MCRName) o;

        if (id != null ? !id.equals(mcrName1.id) : mcrName1.id != null) return false;
        if (notes != null ? !notes.equals(mcrName1.notes) : mcrName1.notes != null) return false;
        if (shortName != null ? !shortName.equals(mcrName1.shortName) : mcrName1.shortName != null) return false;
        if (mcrNumber != null ? !mcrNumber.equals(mcrName1.mcrNumber) : mcrName1.mcrNumber != null) return false;
        return mcrName != null ? mcrName.equals(mcrName1.mcrName) : mcrName1.mcrName == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (notes != null ? notes.hashCode() : 0);
        result = 31 * result + (shortName != null ? shortName.hashCode() : 0);
        result = 31 * result + (mcrNumber != null ? mcrNumber.hashCode() : 0);
        result = 31 * result + (mcrName != null ? mcrName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MCRName{" +
                "id=" + id +
                ", notes='" + notes + '\'' +
                ", shortName='" + shortName + '\'' +
                '}';
    }
}
