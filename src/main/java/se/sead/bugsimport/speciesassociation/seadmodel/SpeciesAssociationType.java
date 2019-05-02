package se.sead.bugsimport.speciesassociation.seadmodel;

import se.sead.sead.model.LoggableEntity;

import javax.persistence.*;

@Entity
@Table(name = "tbl_species_association_types")
@SequenceGenerator(name = "species_association_type_id_gen", sequenceName = "tbl_association_types_association_type_id_seq")
public class SpeciesAssociationType extends LoggableEntity{

    @Id
    @GeneratedValue(generator = "species_association_type_id_gen", strategy = GenerationType.IDENTITY)
    @Column(name = "association_type_id", nullable = false)
    private Integer id;
    @Column(name = "association_type_name")
    private String name;
    @Column(name = "association_description")
    private String description;

    @Override
    public Integer getId() {
        return id;
    }

    protected void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SpeciesAssociationType that = (SpeciesAssociationType) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return description != null ? description.equals(that.description) : that.description == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SpeciesAssociationType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
