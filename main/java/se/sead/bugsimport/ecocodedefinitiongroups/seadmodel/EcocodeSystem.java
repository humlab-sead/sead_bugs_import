package se.sead.bugsimport.ecocodedefinitiongroups.seadmodel;

import se.sead.sead.model.LoggableEntity;

import javax.persistence.*;

@Entity
@Table(name = "tbl_ecocode_systems")
@SequenceGenerator(name = "ecocode_system_gen", sequenceName = "tbl_ecocode_systems_ecocode_system_id_seq")
public class EcocodeSystem extends LoggableEntity {

    @Id
    @GeneratedValue(generator = "ecocode_system_gen", strategy = GenerationType.IDENTITY)
    @Column(name = "ecocode_system_id", nullable = false)
    private Integer id;
    @Column(name = "definition")
    private String definition;
    @Column(name = "name")
    private String name;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EcocodeSystem that = (EcocodeSystem) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (definition != null ? !definition.equals(that.definition) : that.definition != null) return false;
        return name != null ? name.equals(that.name) : that.name == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (definition != null ? definition.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "EcocodeSystem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
