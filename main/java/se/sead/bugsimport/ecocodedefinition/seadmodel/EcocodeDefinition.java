package se.sead.bugsimport.ecocodedefinition.seadmodel;

import se.sead.bugsimport.ecocodedefinitiongroups.seadmodel.EcocodeGroup;
import se.sead.sead.model.LoggableEntity;

import javax.persistence.*;

@Entity
@Table(name="tbl_ecocode_definitions")
@SequenceGenerator(name="ecocode_definition_id_gen", sequenceName = "tbl_ecocode_definitions_ecocode_definition_id_seq")
public class EcocodeDefinition extends LoggableEntity{

    @Id
    @GeneratedValue(generator = "ecocode_definition_id_gen", strategy = GenerationType.AUTO)
    @Column(name = "ecocode_definition_id", nullable = false)
    private Integer id;
    @Column(name = "abbreviation")
    private String abbreviation;
    @Column(name = "definition")
    private String definition;
    @Column(name = "name")
    private String name;
    @Column(name = "notes")
    private String notes;
    @ManyToOne
    @JoinColumn(name = "ecocode_group_id")
    private EcocodeGroup group;

    @Override
    public Integer getId() {
        return id;
    }

    protected void setId(Integer id) {
        this.id = id;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
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

    public EcocodeGroup getGroup() {
        return group;
    }

    public void setGroup(EcocodeGroup group) {
        this.group = group;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof EcocodeDefinition)) return false;

        EcocodeDefinition that = (EcocodeDefinition) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (abbreviation != null ? !abbreviation.equals(that.abbreviation) : that.abbreviation != null) return false;
        if (definition != null ? !definition.equals(that.definition) : that.definition != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (notes != null ? !notes.equals(that.notes) : that.notes != null) return false;
        return group != null ? group.equals(that.group) : that.group == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (abbreviation != null ? abbreviation.hashCode() : 0);
        result = 31 * result + (definition != null ? definition.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (notes != null ? notes.hashCode() : 0);
        result = 31 * result + (group != null ? group.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "EcocodeDefinition{" +
                "id=" + id +
                ", abbreviation='" + abbreviation + '\'' +
                ", definition='" + definition + '\'' +
                ", name='" + name + '\'' +
                ", notes='" + notes + '\'' +
                ", group=" + group +
                '}';
    }
}
