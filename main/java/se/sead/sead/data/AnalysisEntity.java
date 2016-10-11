package se.sead.sead.data;

import se.sead.bugsimport.sample.seadmodel.Sample;
import se.sead.sead.model.LoggableEntity;

import javax.persistence.*;

@Entity
@Table(name = "tbl_analysis_entities")
@SequenceGenerator(name= "analysis_entity_id_gen", sequenceName = "tbl_analysis_entities_analysis_entity_id_seq")
public class AnalysisEntity extends LoggableEntity {

    @Id
    @GeneratedValue(generator = "analysis_entity_id_gen", strategy = GenerationType.IDENTITY)
    @Column(name = "analysis_entity_id", nullable = false)
    private Integer id;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "physical_sample_id")
    private Sample sample;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "dataset_id")
    private Dataset dataset;

    @Override
    public Integer getId() {
        return id;
    }

    protected void setId(Integer id) {
        this.id = id;
    }

    public Sample getSample() {
        return sample;
    }

    public void setSample(Sample sample) {
        this.sample = sample;
    }

    public Dataset getDataset() {
        return dataset;
    }

    public void setDataset(Dataset dataset) {
        this.dataset = dataset;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof AnalysisEntity)) return false;

        AnalysisEntity that = (AnalysisEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (sample != null ? !sample.equals(that.sample) : that.sample != null) return false;
        return dataset != null ? dataset.equals(that.dataset) : that.dataset == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (sample != null ? sample.hashCode() : 0);
        result = 31 * result + (dataset != null ? dataset.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AnalysisEntity{" +
                "id=" + id +
                ", sample=" + sample +
                ", dataset=" + dataset +
                '}';
    }
}
