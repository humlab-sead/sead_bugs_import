package se.sead.bugsimport.datesperiod.seadmodel;

import se.sead.bugsimport.datesradio.seadmodel.DatingUncertainty;
import se.sead.bugsimport.periods.seadmodel.RelativeAge;
import se.sead.bugsimport.sample.seadmodel.Sample;
import se.sead.sead.methods.Method;
import se.sead.sead.model.LoggableEntity;

import javax.persistence.*;

@Entity
@Table(name = "tbl_relative_dates")
@SequenceGenerator(name = "relative_date_id_gen", sequenceName = "tbl_relative_dates_relative_date_id_seq")
public class RelativeDate extends LoggableEntity {

    @Id
    @GeneratedValue(generator = "relative_date_id_gen", strategy = GenerationType.IDENTITY)
    @Column(name = "relative_date_id")
    private Integer id;
    @Column(name = "notes")
    private String notes;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "relative_age_id")
    private RelativeAge relativeAge;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "method_id")
    private Method datingMethod;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "dating_uncertainty_id")
    private DatingUncertainty uncertainty;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "physical_sample_id")
    private Sample sample;

    @Override
    public Integer getId() {
        return id;
    }

    protected void setId(Integer id) {
        this.id = id;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public RelativeAge getRelativeAge() {
        return relativeAge;
    }

    public void setRelativeAge(RelativeAge relativeAge) {
        this.relativeAge = relativeAge;
    }

    public Method getDatingMethod() {
        return datingMethod;
    }

    public void setDatingMethod(Method datingMethod) {
        this.datingMethod = datingMethod;
    }

    public DatingUncertainty getUncertainty() {
        return uncertainty;
    }

    public void setUncertainty(DatingUncertainty uncertainty) {
        this.uncertainty = uncertainty;
    }

    public Sample getSample() {
        return sample;
    }

    public void setSample(Sample sample) {
        this.sample = sample;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof RelativeDate)) return false;

        RelativeDate that = (RelativeDate) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (notes != null ? !notes.equals(that.notes) : that.notes != null) return false;
        if (relativeAge != null ? !relativeAge.equals(that.relativeAge) : that.relativeAge != null) return false;
        if (datingMethod != null ? !datingMethod.equals(that.datingMethod) : that.datingMethod != null) return false;
        if (uncertainty != null ? !uncertainty.equals(that.uncertainty) : that.uncertainty != null) return false;
        return sample != null ? sample.equals(that.sample) : that.sample == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (notes != null ? notes.hashCode() : 0);
        result = 31 * result + (relativeAge != null ? relativeAge.hashCode() : 0);
        result = 31 * result + (datingMethod != null ? datingMethod.hashCode() : 0);
        result = 31 * result + (uncertainty != null ? uncertainty.hashCode() : 0);
        result = 31 * result + (sample != null ? sample.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RelativeDate{" +
                "id=" + id +
                ", notes='" + notes + '\'' +
                ", relativeAge=" + relativeAge +
                ", datingMethod=" + datingMethod +
                ", uncertainty=" + uncertainty +
                ", sample=" + sample +
                '}';
    }
}
