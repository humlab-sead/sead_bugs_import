package se.sead.bugsimport.ecocodedefinition.seadmodel;

import se.sead.sead.model.LoggableEntity;

import javax.persistence.*;

@Entity
@Table(name = "tbl_ecocode_groups")
@SequenceGenerator(name = "ecocode_group_id_gen", sequenceName = "tbl_ecocode_groups_ecocode_group_id_seq")
public class EcocodeGroup extends LoggableEntity{

    @Id
    @GeneratedValue(generator = "ecocode_group_id_gen", strategy = GenerationType.AUTO)
    @Column(name = "ecocode_group_id", nullable = false)
    private Integer id;
    @Column(name = "definition")
    private String definition;
    @Column(name = "name")
    private String label;
    @ManyToOne
    @JoinColumn(name = "ecocode_system_id")
    private EcocodeSystem system;

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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public EcocodeSystem getSystem() {
        return system;
    }

    public void setSystem(EcocodeSystem system) {
        this.system = system;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EcocodeGroup that = (EcocodeGroup) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (definition != null ? !definition.equals(that.definition) : that.definition != null) return false;
        if (label != null ? !label.equals(that.label) : that.label != null) return false;
        return system != null ? system.equals(that.system) : that.system == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (definition != null ? definition.hashCode() : 0);
        result = 31 * result + (label != null ? label.hashCode() : 0);
        result = 31 * result + (system != null ? system.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "EcocodeGroup{" +
                "id=" + id +
                ",label=" + label +
                '}';
    }
}
