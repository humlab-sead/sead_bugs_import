package se.sead.sead.data;

import se.sead.sead.model.LoggableEntity;

import javax.persistence.*;

@Entity
@Table(name = "tbl_data_types")
@SequenceGenerator(name="data_type_id_gen", sequenceName = "tbl_data_types_data_type_id_seq")
public class DataType extends LoggableEntity {

    @Id
    @GeneratedValue(generator = "data_type_id_gen", strategy = GenerationType.IDENTITY)
    @Column(name = "data_type_id", nullable = false)
    private Integer id;
    @Column(name = "data_type_name")
    private String name;
    @Column(name = "definition")
    private String description;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "data_type_group_id")
    private DataTypeGroup typeGroup;

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

    public DataTypeGroup getTypeGroup() {
        return typeGroup;
    }

    public void setTypeGroup(DataTypeGroup typeGroup) {
        this.typeGroup = typeGroup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataType dataType = (DataType) o;

        if (id != null ? !id.equals(dataType.id) : dataType.id != null) return false;
        if (name != null ? !name.equals(dataType.name) : dataType.name != null) return false;
        if (description != null ? !description.equals(dataType.description) : dataType.description != null)
            return false;
        return typeGroup != null ? typeGroup.equals(dataType.typeGroup) : dataType.typeGroup == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (typeGroup != null ? typeGroup.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DataType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", typeGroup=" + typeGroup +
                '}';
    }
}
