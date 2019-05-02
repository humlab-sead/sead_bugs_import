package se.sead.bugsimport.rdbcodes.seadmodel;

import se.sead.bugsimport.rdbsystems.seadmodel.RdbSystem;
import se.sead.sead.model.LoggableEntity;

import javax.persistence.*;

@Entity
@Table(name="tbl_rdb_codes")
@SequenceGenerator(name="rdb_code_id_gen", sequenceName = "tbl_rdb_codes_rdb_code_id_seq")
public class RdbCode extends LoggableEntity {

    @Id
    @GeneratedValue(generator = "rdb_code_id_gen", strategy = GenerationType.IDENTITY)
    @Column(name="rdb_code_id", nullable = false)
    private Integer id;
    @Column(name="rdb_category")
    private String category;
    @Column(name="rdb_definition")
    private String definition;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.MERGE})
    @JoinColumn(name = "rdb_system_id")
    private RdbSystem system;

    @Override
    public Integer getId() {
        return id;
    }

    protected void setId(Integer id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public RdbSystem getSystem() {
        return system;
    }

    public void setSystem(RdbSystem system) {
        this.system = system;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof RdbCode)) return false;

        RdbCode rdbCode = (RdbCode) o;

        if (id != null ? !id.equals(rdbCode.id) : rdbCode.id != null) return false;
        if (category != null ? !category.equals(rdbCode.category) : rdbCode.category != null) return false;
        if (definition != null ? !definition.equals(rdbCode.definition) : rdbCode.definition != null) return false;
        return system != null ? system.equals(rdbCode.system) : rdbCode.system == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (definition != null ? definition.hashCode() : 0);
        result = 31 * result + (system != null ? system.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RdbCode{" +
                "id=" + id +
                ", category='" + category + '\'' +
                ", definition='" + definition + '\'' +
                '}';
    }
}
