package se.sead.bugsimport.periods.seadmodel;

import se.sead.sead.model.LoggableEntity;

import javax.persistence.*;

@Entity
@Table(name = "tbl_relative_age_types")
@SequenceGenerator(name = "relative_age_type_id", sequenceName = "tbl_relative_age_types_relative_age_type_id_seq")
public class RelativeAgeType extends LoggableEntity {

    @Id
    @GeneratedValue(generator = "relative_age_type_id", strategy = GenerationType.IDENTITY)
    @Column(name = "relative_age_type_id", nullable = false)
    private Integer id;
    @Column(name = "age_type")
    private String type;
    @Column(name = "description")
    private String description;

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

        RelativeAgeType that = (RelativeAgeType) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        return description != null ? description.equals(that.description) : that.description == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RelativeAgeType{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
