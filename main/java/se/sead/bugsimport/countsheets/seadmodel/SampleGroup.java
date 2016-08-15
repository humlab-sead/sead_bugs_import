package se.sead.bugsimport.countsheets.seadmodel;

import se.sead.bugsimport.site.seadmodel.SeadSite;
import se.sead.sead.model.LoggableEntity;
import se.sead.sead.methods.Method;
import se.sead.sead.model.SamplingContext;

import javax.persistence.*;

@Entity
@Table(name = "tbl_sample_groups")
@SequenceGenerator(name="sample_group_id_seq", sequenceName = "tbl_sample_groups_sample_group_id_seq")
public class SampleGroup extends LoggableEntity{

    @Id
    @GeneratedValue(generator = "sample_group_id_seq", strategy = GenerationType.AUTO)
    @Column(name="sample_group_id", nullable = false)
    private Integer id;
    @Column(name="sample_group_name", nullable = false)
    private String name;
    @Column(name="sample_group_description")
    private String description;
    @ManyToOne(optional = false)
    @JoinColumn(name="site_id")
    private SeadSite site;
    @ManyToOne
    @JoinColumn(name="sampling_context_id")
    private SamplingContext samplingContext;
    @ManyToOne(optional = false)
    @JoinColumn(name="method_id", nullable = false)
    private Method samplingMethod;

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

    public SeadSite getSite() {
        return site;
    }

    public void setSite(SeadSite site) {
        this.site = site;
    }

    public SamplingContext getSamplingContext() {
        return samplingContext;
    }

    public void setSamplingContext(SamplingContext samplingContext) {
        this.samplingContext = samplingContext;
    }

    public Method getSamplingMethod() {
        return samplingMethod;
    }

    public void setSamplingMethod(Method samplingMethod) {
        this.samplingMethod = samplingMethod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof SampleGroup)) return false;

        SampleGroup that = (SampleGroup) o;

        if (id != null && that.id != null && !id.equals(that.id)) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (site != null ? !site.equals(that.site) : that.site != null) return false;
        if (samplingContext != null ? !samplingContext.equals(that.samplingContext) : that.samplingContext != null)
            return false;
        return samplingMethod != null ? samplingMethod.equals(that.samplingMethod) : that.samplingMethod == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (site != null ? site.hashCode() : 0);
        result = 31 * result + (samplingContext != null ? samplingContext.hashCode() : 0);
        result = 31 * result + (samplingMethod != null ? samplingMethod.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SampleGroup{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", site=" + site +
                ", samplingContext=" + samplingContext +
                ", samplingMethod=" + samplingMethod +
                '}';
    }
}
