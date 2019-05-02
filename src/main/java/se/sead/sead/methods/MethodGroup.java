package se.sead.sead.methods;

import se.sead.sead.model.LoggableEntity;

import javax.persistence.*;

@Entity
@Table(name="tbl_method_groups")
@SequenceGenerator(name="method_group_id_seq", sequenceName = "tbl_method_groups_method_group_id_seq")
public class MethodGroup extends LoggableEntity {

    @Id
    @GeneratedValue(generator = "method_group_id_seq", strategy = GenerationType.IDENTITY)
    @Column(name="method_group_id", nullable = false)
    private Integer id;
    @Column(name="group_name", nullable = false)
    private String name;
    @Column(name="description", nullable = false)
    private String description;

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
    public Integer getId() {
        return id;
    }

    protected void setId(Integer id){
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof MethodGroup)) return false;

        MethodGroup that = (MethodGroup) o;

        if (!id.equals(that.id)) return false;
        if (!name.equals(that.name)) return false;
        return description.equals(that.description);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + description.hashCode();
        return result;
    }
}
