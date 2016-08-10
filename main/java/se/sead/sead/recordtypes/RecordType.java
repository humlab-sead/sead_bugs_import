package se.sead.sead.recordtypes;

import se.sead.sead.model.LoggableEntity;

import javax.persistence.*;

@Entity
@Table(name = "tbl_record_types")
@SequenceGenerator(name="record_type_id_seq", sequenceName = "tbl_record_types_record_type_id_seq")
public class RecordType extends LoggableEntity {

    @Id
    @GeneratedValue(generator = "record_type_id_seq", strategy = GenerationType.AUTO)
    @Column(name="record_type_id", nullable = false)
    private Integer id;

    @Column(name="record_type_name")
    private String name;
    @Column(name="record_type_description")
    private String description;

    @Override
    public Integer getId() {
        return id;
    }

    protected void setId(Integer id){
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

        RecordType that = (RecordType) o;

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
        return "RecordType{" + name + '}';
    }
}
