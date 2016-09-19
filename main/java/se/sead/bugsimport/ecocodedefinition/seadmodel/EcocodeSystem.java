package se.sead.bugsimport.ecocodedefinition.seadmodel;

import se.sead.sead.model.Biblio;
import se.sead.sead.model.LoggableEntity;

import javax.persistence.*;

@Entity
@Table(name = "tbl_ecocode_systems")
@SequenceGenerator(name = "ecocode_system_id_gen", sequenceName = "tbl_ecocode_systems_ecocode_system_id_seq")
public class EcocodeSystem extends LoggableEntity {

    @Id
    @GeneratedValue(generator = "ecocode_system_id_gen", strategy = GenerationType.AUTO)
    @Column(name = "ecocode_system_id", nullable = false)
    private Integer id;
    @Column(name = "definition")
    private String definition;
    @Column(name = "name")
    private String name;
    @Column(name = "notes")
    private String notes;
    @ManyToOne
    @JoinColumn(name = "biblio_id")
    private Biblio reference;

    @Override
    public Integer getId() {
        return id;
    }

    protected void setId(Integer id) {
        this.id = id;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EcocodeSystem that = (EcocodeSystem) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (definition != null ? !definition.equals(that.definition) : that.definition != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (notes != null ? !notes.equals(that.notes) : that.notes != null) return false;
        return reference != null ? reference.equals(that.reference) : that.reference == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (definition != null ? definition.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (notes != null ? notes.hashCode() : 0);
        result = 31 * result + (reference != null ? reference.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "EcocodeSystem{" +
                "id=" + id +
                '}';
    }
}
