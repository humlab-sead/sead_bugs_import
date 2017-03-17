package se.sead.bugsimport.taxanotes.seadmodel;

import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.bugsimport.bibliography.seadmodel.Biblio;
import se.sead.sead.model.LoggableEntity;

import javax.persistence.*;

@Entity
@Table(name="tbl_taxonomy_notes")
@SequenceGenerator(name = "taxonomy_notes_id_seq", sequenceName = "tbl_taxonomy_notes_taxonomy_notes_id_seq")
public class TaxonomicNotes extends LoggableEntity{

    @Id
    @GeneratedValue(generator = "taxonomy_notes_id_seq", strategy = GenerationType.IDENTITY)
    @Column(name="taxonomy_notes_id", nullable = false)
    private Integer id;
    @Column(name="taxonomy_notes")
    private String notes;
    @ManyToOne
    @JoinColumn(name="biblio_id", nullable = false)
    private Biblio reference;
    @ManyToOne
    @JoinColumn(name="taxon_id", nullable = false)
    private TaxaSpecies species;

    @Override
    public Integer getId() {
        return id;
    }

    protected void setId(Integer id){
        this.id = id;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Biblio getReference() {
        return reference;
    }

    public void setReference(Biblio reference) {
        this.reference = reference;
    }

    public TaxaSpecies getSpecies() {
        return species;
    }

    public void setSpecies(TaxaSpecies species) {
        this.species = species;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof TaxonomicNotes)) return false;

        TaxonomicNotes that = (TaxonomicNotes) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (notes != null ? !notes.equals(that.notes) : that.notes != null) return false;
        if (!reference.equals(that.reference)) return false;
        return species.equals(that.species);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (notes != null ? notes.hashCode() : 0);
        result = 31 * result + reference.hashCode();
        result = 31 * result + species.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "TaxonomicNotes{" +
                "id=" + id +
                ", notes='" + notes + '\'' +
                ", reference=" + reference +
                ", species=" + species +
                '}';
    }
}
