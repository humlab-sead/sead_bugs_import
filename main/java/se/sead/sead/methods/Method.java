package se.sead.sead.methods;

import se.sead.sead.model.Biblio;
import se.sead.sead.model.LoggableEntity;
import se.sead.sead.model.Unit;
import se.sead.sead.recordtypes.RecordType;

import javax.persistence.*;

@Entity
@Table(name="tbl_methods")
@SequenceGenerator(name="method_id_seq", sequenceName = "tbl_methods_method_id_seq")
public class Method extends LoggableEntity {

    @Id
    @GeneratedValue(generator = "method_id_seq", strategy = GenerationType.IDENTITY)
    @Column(name="method_id", nullable = false)
    private Integer id;
    @Column(name="method_name", nullable = false)
    private String name;
    @Column(name="method_abbrev_or_alt_name", nullable = false)
    private String abbreviation;
    @Column(name="description", nullable = false)
    private String description;
    @ManyToOne(optional = false)
    @JoinColumn(name="method_group_id", nullable = false)
    private MethodGroup group;
    @ManyToOne
    @JoinColumn(name="biblio_id")
    private Biblio reference;
    @ManyToOne
    @JoinColumn(name="record_type_id")
    private RecordType recordType;
    @ManyToOne
    @JoinColumn(name="unit_id")
    private Unit unit;

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

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MethodGroup getGroup() {
        return group;
    }

    public void setGroup(MethodGroup group) {
        this.group = group;
    }

    public Biblio getReference() {
        return reference;
    }

    public void setReference(Biblio reference) {
        this.reference = reference;
    }

    public RecordType getRecordType() {
        return recordType;
    }

    public void setRecordType(RecordType recordType) {
        this.recordType = recordType;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Method)) return false;

        Method method = (Method) o;

        if (id != null ? !id.equals(method.id) : method.id != null) return false;
        if (name != null ? !name.equals(method.name) : method.name != null) return false;
        if (abbreviation != null ? !abbreviation.equals(method.abbreviation) : method.abbreviation != null)
            return false;
        if (description != null ? !description.equals(method.description) : method.description != null) return false;
        if (group != null ? !group.equals(method.group) : method.group != null) return false;
        if (reference != null ? !reference.equals(method.reference) : method.reference != null) return false;
        if (recordType != null ? !recordType.equals(method.recordType) : method.recordType != null) return false;
        return unit != null ? unit.equals(method.unit) : method.unit == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (abbreviation != null ? abbreviation.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (group != null ? group.hashCode() : 0);
        result = 31 * result + (reference != null ? reference.hashCode() : 0);
        result = 31 * result + (recordType != null ? recordType.hashCode() : 0);
        result = 31 * result + (unit != null ? unit.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Method{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", abbreviation='" + abbreviation + '\'' +
                '}';
    }
}
