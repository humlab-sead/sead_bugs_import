package se.sead.sead.model;

import se.sead.sead.methods.MethodGroup;

import javax.persistence.*;

@Entity
@Table(name = "tbl_dimensions")
@SequenceGenerator(name = "dimension_id_gen", sequenceName = "tbl_dimensions_dimension_id_seq")
public class Dimension extends LoggableEntity {

    @Id
    @GeneratedValue(generator = "dimension_id_gen", strategy = GenerationType.IDENTITY)
    @Column(name = "dimension_id")
    private Integer id;
    @Column(name = "dimension_abbrev")
    private String abbrev;
    @Column(name = "dimension_description")
    private String description;
    @Column(name = "dimension_name")
    private String name;
    @ManyToOne
    @JoinColumn(name = "unit_id")
    private Unit unit;
    @ManyToOne
    @JoinColumn(name = "method_group_id")
    private MethodGroup methodGroup;

    @Override
    public Integer getId() {
        return id;
    }

    protected void setId(Integer id) {
        this.id = id;
    }

    public String getAbbrev() {
        return abbrev;
    }

    public void setAbbrev(String abbrev) {
        this.abbrev = abbrev;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public MethodGroup getMethodGroup() {
        return methodGroup;
    }

    public void setMethodGroup(MethodGroup methodGroup) {
        this.methodGroup = methodGroup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Dimension dimension = (Dimension) o;

        if (id != null ? !id.equals(dimension.id) : dimension.id != null) return false;
        if (abbrev != null ? !abbrev.equals(dimension.abbrev) : dimension.abbrev != null) return false;
        if (description != null ? !description.equals(dimension.description) : dimension.description != null)
            return false;
        if (name != null ? !name.equals(dimension.name) : dimension.name != null) return false;
        if (unit != null ? !unit.equals(dimension.unit) : dimension.unit != null) return false;
        return methodGroup != null ? methodGroup.equals(dimension.methodGroup) : dimension.methodGroup == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (abbrev != null ? abbrev.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (unit != null ? unit.hashCode() : 0);
        result = 31 * result + (methodGroup != null ? methodGroup.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Dimension{" +
                "id=" + id +
                ", abbrev='" + abbrev + '\'' +
                '}';
    }
}
