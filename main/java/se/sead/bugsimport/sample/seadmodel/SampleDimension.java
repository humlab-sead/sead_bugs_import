package se.sead.bugsimport.sample.seadmodel;

import se.sead.sead.methods.Method;
import se.sead.sead.model.Dimension;
import se.sead.sead.model.LoggableEntity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "tbl_sample_dimensions")
@SequenceGenerator(name = "sample_dimension_id_gen", sequenceName = "tbl_sample_dimensions_sample_dimension_id_seq")
public class SampleDimension extends LoggableEntity{

    @Id
    @GeneratedValue(generator = "sample_dimension_id_gen", strategy = GenerationType.IDENTITY)
    @Column(name = "sample_dimension_id", nullable = false)
    private Integer id;
    @Column(name = "dimension_value", precision = 20, scale = 10)
    private BigDecimal value;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "physical_sample_id")
    private Sample sample;
    @ManyToOne
    @JoinColumn(name = "dimension_id")
    private Dimension dimension;
    @ManyToOne
    @JoinColumn(name = "method_id")
    private Method method;

    @Override
    public Integer getId() {
        return id;
    }

    protected void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Sample getSample() {
        return sample;
    }

    public void setSample(Sample sample) {
        this.sample = sample;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof SampleDimension)) return false;

        SampleDimension that = (SampleDimension) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;
        if (sample != null ? !sample.equals(that.sample) : that.sample != null) return false;
        if (dimension != null ? !dimension.equals(that.dimension) : that.dimension != null) return false;
        return method != null ? method.equals(that.method) : that.method == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (sample != null ? sample.hashCode() : 0);
        result = 31 * result + (dimension != null ? dimension.hashCode() : 0);
        result = 31 * result + (method != null ? method.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SampleDimension{" +
                "id=" + id +
                ", value=" + value +
                '}';
    }
}
