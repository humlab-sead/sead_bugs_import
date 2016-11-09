package se.sead.sead.model;

import javax.persistence.*;

@Entity
@Table(name="tbl_units")
@SequenceGenerator(name="unit_id_seq", sequenceName = "tbl_units_unit_id_seq")
public class Unit extends LoggableEntity {

    @Id
    @GeneratedValue(generator = "unit_id_seq", strategy = GenerationType.IDENTITY)
    @Column(name="unit_id", nullable = false)
    private Integer id;
    @Column(name="unit_name", nullable = false, length = 50)
    private String name;
    @Column(name="description")
    private String description;
    @Column(name="unit_abbrev", length = 15)
    private String abbreviation;

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

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    @Override
    public Integer getId() {
        return id;
    }

    protected void setId(Integer id){
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Unit)) return false;

        Unit unit = (Unit) o;

        if (!id.equals(unit.id)) return false;
        if (!name.equals(unit.name)) return false;
        if (description != null ? !description.equals(unit.description) : unit.description != null) return false;
        return abbreviation != null ? abbreviation.equals(unit.abbreviation) : unit.abbreviation == null;

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (abbreviation != null ? abbreviation.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Unit{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
