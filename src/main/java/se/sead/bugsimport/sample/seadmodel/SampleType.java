package se.sead.bugsimport.sample.seadmodel;

import se.sead.sead.model.LoggableEntity;

import javax.persistence.*;

@Entity
@Table(name = "tbl_sample_types")
@SequenceGenerator(name = "sample_type_id_gen", sequenceName = "tbl_sample_types_sample_type_id_seq")
public class SampleType extends LoggableEntity{

    @Id
    @GeneratedValue(generator = "sample_type_id_gen", strategy = GenerationType.IDENTITY)
    @Column(name = "sample_type_id", nullable = false)
    private Integer id;
    @Column(name = "type_name", nullable = false)
    private String name;
    @Column(name = "description")
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
        if (o == null || !(o instanceof SampleType)) return false;

        SampleType that = (SampleType) o;

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
        return "SampleType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
