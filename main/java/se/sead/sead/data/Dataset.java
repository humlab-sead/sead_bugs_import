package se.sead.sead.data;

import se.sead.sead.methods.Method;
import se.sead.sead.model.LoggableEntity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tbl_datasets")
@SequenceGenerator(name = "dataset_id_gen", sequenceName = "tbl_datasets_dataset_id_seq")
public class Dataset extends LoggableEntity {

    @Id
    @GeneratedValue(generator = "dataset_id_gen", strategy = GenerationType.IDENTITY)
    @Column(name = "dataset_id", nullable = false)
    private Integer id;
    @Column(name = "dataset_name")
    private String name;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "data_type_id")
    private DataType dataType;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "master_set_id")
    private DatasetMaster masterDataset;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "method_id")
    private Method method;
    @OneToMany(mappedBy = "dataset")
    private List<DatasetContact> contacts;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name="updated_dataset_id")
    private Dataset updatedDataset;

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

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public DatasetMaster getMasterDataset() {
        return masterDataset;
    }

    public void setMasterDataset(DatasetMaster masterDataset) {
        this.masterDataset = masterDataset;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public List<DatasetContact> getContacts() {
        return contacts;
    }

    public void setContacts(List<DatasetContact> contacts) {
        this.contacts = contacts;
    }

    public Dataset getUpdatedDataset() {
        return updatedDataset;
    }

    public void setUpdatedDataset(Dataset updatedDataset) {
        this.updatedDataset = updatedDataset;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Dataset)) return false;

        Dataset dataset = (Dataset) o;

        if (id != null ? !id.equals(dataset.id) : dataset.id != null) return false;
        if (name != null ? !name.equals(dataset.name) : dataset.name != null) return false;
        if (dataType != null ? !dataType.equals(dataset.dataType) : dataset.dataType != null) return false;
        if (masterDataset != null ? !masterDataset.equals(dataset.masterDataset) : dataset.masterDataset != null)
            return false;
        if (method != null ? !method.equals(dataset.method) : dataset.method != null) return false;
        return updatedDataset != null ? updatedDataset.equals(dataset.updatedDataset) : dataset.updatedDataset == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (dataType != null ? dataType.hashCode() : 0);
        result = 31 * result + (masterDataset != null ? masterDataset.hashCode() : 0);
        result = 31 * result + (method != null ? method.hashCode() : 0);
        result = 31 * result + (updatedDataset != null ? updatedDataset.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Dataset{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dataType=" + dataType +
                ", masterDataset=" + masterDataset +
                ", method=" + method +
                ", updated=" + updatedDataset +
                '}';
    }
}
