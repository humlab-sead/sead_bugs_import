package se.sead.bugsimport.ecocodedefinitiongroups.seadmodel;

import se.sead.sead.model.LoggableEntity;

import javax.persistence.*;

@Entity
@Table(name = "tbl_ecocode_groups")
@SequenceGenerator(name = "ecocode_group_id_gen", sequenceName = "tbl_ecocode_groups_ecocode_group_id_seq")
public class EcocodeGroup extends LoggableEntity{

    @Id
    @GeneratedValue(generator = "ecocode_group_id_gen", strategy = GenerationType.IDENTITY)
    @Column(name = "ecocode_group_id", nullable = false)
    private Integer id;
    @Column(name = "definition")
    private String definition;
    @Column(name = "name")
    private String name;
    @Column(name = "abbreviation")
    private String abbreviation;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
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
        if (o == null || !(o instanceof EcocodeGroup)) return false;

        EcocodeGroup that = (EcocodeGroup) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (definition != null ? !definition.equals(that.definition) : that.definition != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (abbreviation != null ? !abbreviation.equals(that.abbreviation) : that.abbreviation != null) return false;
        return system != null ? system.equals(that.system) : that.system == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (definition != null ? definition.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (abbreviation != null ? abbreviation.hashCode() : 0);
        result = 31 * result + (system != null ? system.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "EcocodeGroup{" +
                "id=" + id +
                ", definition='" + definition + '\'' +
                ", name='" + name + '\'' +
                ", abbreviation='" + abbreviation + '\'' +
                ", system=" + system +
                '}';
    }
}
