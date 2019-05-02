package se.sead.bugsimport.datesradio.seadmodel;

import se.sead.bugsimport.lab.seadmodel.DatingLab;
import se.sead.sead.data.AnalysisEntity;
import se.sead.sead.model.LoggableEntity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "tbl_geochronology")
@SequenceGenerator(name = "geochron_id_gen", sequenceName = "tbl_geochronology_geochron_id_seq")
public class Geochronology extends LoggableEntity {

    @Id
    @GeneratedValue(generator = "geochron_id_gen", strategy = GenerationType.IDENTITY)
    @Column(name = "geochron_id", nullable = false)
    private Integer id;
    @Column(name = "age", precision = 20, scale = 5)
    private BigDecimal age;
    @Column(name = "error_older", precision = 20, scale = 5)
    private BigDecimal errorOlder;
    @Column(name = "error_younger", precision = 20, scale = 5)
    private BigDecimal errorYounger;
    @Column(name = "notes")
    private String notes;
    @Column(name = "lab_number")
    private String labSampleNumber;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "dating_lab_id")
    private DatingLab datingLaboratory;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "dating_uncertainty_id")
    private DatingUncertainty uncertainty;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "analysis_entity_id")
    private AnalysisEntity analysisEntity;

    @Override
    public Integer getId() {
        return id;
    }

    protected void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getAge() {
        return age;
    }

    public void setAge(BigDecimal age) {
        this.age = age;
    }

    public BigDecimal getErrorOlder() {
        return errorOlder;
    }

    public void setErrorOlder(BigDecimal errorOlder) {
        this.errorOlder = errorOlder;
    }

    public BigDecimal getErrorYounger() {
        return errorYounger;
    }

    public void setErrorYounger(BigDecimal errorYounger) {
        this.errorYounger = errorYounger;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getLabSampleNumber() {
        return labSampleNumber;
    }

    public void setLabSampleNumber(String labSampleNumber) {
        this.labSampleNumber = labSampleNumber;
    }

    public DatingLab getDatingLaboratory() {
        return datingLaboratory;
    }

    public void setDatingLaboratory(DatingLab datingLaboratory) {
        this.datingLaboratory = datingLaboratory;
    }

    public DatingUncertainty getUncertainty() {
        return uncertainty;
    }

    public void setUncertainty(DatingUncertainty uncertainty) {
        this.uncertainty = uncertainty;
    }

    public AnalysisEntity getAnalysisEntity() {
        return analysisEntity;
    }

    public void setAnalysisEntity(AnalysisEntity analysisEntity) {
        this.analysisEntity = analysisEntity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Geochronology)) return false;

        Geochronology that = (Geochronology) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (age != null ? !age.equals(that.age) : that.age != null) return false;
        if (errorOlder != null ? !errorOlder.equals(that.errorOlder) : that.errorOlder != null) return false;
        if (errorYounger != null ? !errorYounger.equals(that.errorYounger) : that.errorYounger != null) return false;
        if (notes != null ? !notes.equals(that.notes) : that.notes != null) return false;
        if (labSampleNumber != null ? !labSampleNumber.equals(that.labSampleNumber) : that.labSampleNumber != null)
            return false;
        if (datingLaboratory != null ? !datingLaboratory.equals(that.datingLaboratory) : that.datingLaboratory != null)
            return false;
        if (uncertainty != null ? !uncertainty.equals(that.uncertainty) : that.uncertainty != null) return false;
        return analysisEntity != null ? analysisEntity.equals(that.analysisEntity) : that.analysisEntity == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (age != null ? age.hashCode() : 0);
        result = 31 * result + (errorOlder != null ? errorOlder.hashCode() : 0);
        result = 31 * result + (errorYounger != null ? errorYounger.hashCode() : 0);
        result = 31 * result + (notes != null ? notes.hashCode() : 0);
        result = 31 * result + (labSampleNumber != null ? labSampleNumber.hashCode() : 0);
        result = 31 * result + (datingLaboratory != null ? datingLaboratory.hashCode() : 0);
        result = 31 * result + (uncertainty != null ? uncertainty.hashCode() : 0);
        result = 31 * result + (analysisEntity != null ? analysisEntity.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Geochronology{" +
                "id=" + id +
                ", age=" + age +
                ", errorOlder=" + errorOlder +
                ", errorYounger=" + errorYounger +
                ", notes='" + notes + '\'' +
                ", labSampleNumber='" + labSampleNumber + '\'' +
                ", datingLaboratory=" + datingLaboratory +
                ", uncertainty=" + uncertainty +
                ", analysisEntity=" + analysisEntity +
                '}';
    }
}
