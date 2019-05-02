package se.sead.bugsimport.taxaseasonality.seadmodel;

import se.sead.sead.model.LoggableEntity;

import javax.persistence.*;

@Entity
@Table(name="tbl_activity_types")
@SequenceGenerator(name = "activity_type_id_gen", sequenceName = "tbl_activity_types_activity_type_id_seq")
public class ActivityType extends LoggableEntity{

    @Id
    @GeneratedValue(generator = "activity_type_id_gen", strategy = GenerationType.IDENTITY)
    @Column(name="activity_type_id", nullable = false)
    private Integer id;
    @Column(name="activity_type")
    private String name;
    @Column(name="description")
    private String description;

    @Override
    public Integer getId() {
        return id;
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
        if (o == null || !(o instanceof ActivityType)) return false;

        ActivityType that = (ActivityType) o;

        if (id != null && that.id != null && !id.equals(that.id)) return false;
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
        return "ActivityType{" +
                "name='" + name + '\'' +
                '}';
    }
}
