package se.sead.sead.data;

import se.sead.sead.model.LoggableEntity;

import javax.persistence.*;

@Entity
@Table(name = "tbl_dataset_masters")
@SequenceGenerator(name = "dataset_master_id_gen", sequenceName = "tbl_dataset_masters_master_set_id_seq")
public class DatasetMaster extends LoggableEntity {

    @Id
    @GeneratedValue(generator = "dataset_master_id_gen", strategy = GenerationType.IDENTITY)
    @Column(name = "master_set_id", nullable = false)
    private Integer id;
    @Column(name = "master_name")
    private String name;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DatasetMaster that = (DatasetMaster) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        return name != null ? name.equals(that.name) : that.name == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DatasetMaster{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
