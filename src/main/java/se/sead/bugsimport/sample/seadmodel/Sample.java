package se.sead.bugsimport.sample.seadmodel;

import se.sead.bugsimport.countsheets.seadmodel.SampleGroup;
import se.sead.sead.model.LoggableEntity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tbl_physical_samples")
@SequenceGenerator(name="physical_sample_id_gen", sequenceName = "tbl_physical_samples_physical_sample_id_seq")
public class Sample extends LoggableEntity {

    @Id
    @GeneratedValue(generator = "physical_sample_id_gen", strategy = GenerationType.IDENTITY)
    @Column(name = "physical_sample_id", nullable = false)
    private Integer id;
    @Column(name = "sample_name", length = 50, nullable = false)
    private String name;
    @Column(name = "date_sampled")
    private String dateSampled;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "sample_group_id")
    private SampleGroup group;
    @ManyToOne
    @JoinColumn(name = "alt_ref_type_id")
    private AlternativeReferenceType referenceType;
    @ManyToOne
    @JoinColumn(name = "sample_type_id", nullable = false)
    private SampleType type;
    @OneToMany(
            fetch = FetchType.EAGER,
            mappedBy = "sample"
    )
    private List<SampleDimension> dimensions;

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

    public String getDateSampled() {
        return dateSampled;
    }

    public void setDateSampled(String dateSampled) {
        this.dateSampled = dateSampled;
    }

    public SampleGroup getGroup() {
        return group;
    }

    public void setGroup(SampleGroup group) {
        this.group = group;
    }

    public AlternativeReferenceType getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(AlternativeReferenceType referenceType) {
        this.referenceType = referenceType;
    }

    public SampleType getType() {
        return type;
    }

    public void setType(SampleType type) {
        this.type = type;
    }

    public List<SampleDimension> getDimensions() {
        return dimensions;
    }

    public void setDimensions(List<SampleDimension> dimensions) {
        this.dimensions = dimensions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Sample)) return false;

        Sample sample = (Sample) o;

        if (id != null ? !id.equals(sample.id) : sample.id != null) return false;
        if (name != null ? !name.equals(sample.name) : sample.name != null) return false;
        if (dateSampled != null ? !dateSampled.equals(sample.dateSampled) : sample.dateSampled != null) return false;
        if (group != null ? !group.equals(sample.group) : sample.group != null) return false;
        if (referenceType != null ? !referenceType.equals(sample.referenceType) : sample.referenceType != null)
            return false;
        return type != null ? type.equals(sample.type) : sample.type == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (dateSampled != null ? dateSampled.hashCode() : 0);
        result = 31 * result + (group != null ? group.hashCode() : 0);
        result = 31 * result + (referenceType != null ? referenceType.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Sample{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", group=" + group +
                ", dimensions=" + dimensions +
                '}';
    }
}
