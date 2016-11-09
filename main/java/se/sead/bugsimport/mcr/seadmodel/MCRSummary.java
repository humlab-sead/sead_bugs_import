package se.sead.bugsimport.mcr.seadmodel;

import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.sead.model.LoggableEntity;

import javax.persistence.*;

@Entity
@Table(name="tbl_mcr_summary_data")
@SequenceGenerator(name="mcr_summary_seq", sequenceName = "tbl_mcr_summary_data_mcr_summary_data_id_seq")
public class MCRSummary extends LoggableEntity {

    @Id
    @GeneratedValue(generator = "mcr_summary_seq", strategy = GenerationType.IDENTITY)
    @Column(name="mcr_summary_data_id", nullable = false)
    private Integer id;
    @Column(name="tmax_lo")
    private Short maxLo;
    @Column(name="tmax_hi")
    private Short maxHi;
    @Column(name="tmin_lo")
    private Short minLo;
    @Column(name="tmin_hi")
    private Short minHi;
    @Column(name="trange_lo")
    private Short rangeLo;
    @Column(name="trange_hi")
    private Short rangeHi;
    @Column(name="cog_mid_tmax")
    private Short cogMidMax;
    @Column(name="cog_mid_trange")
    private Short cogMidRange;
    @ManyToOne
    @JoinColumn(name="taxon_id")
    private TaxaSpecies species;

    @Override
    public Integer getId() {
        return id;
    }

    protected void setId(Integer id){
        this.id = id;
    }

    public Short getMaxLo() {
        return maxLo;
    }

    public void setMaxLo(Short maxLo) {
        this.maxLo = maxLo;
    }

    public Short getMaxHi() {
        return maxHi;
    }

    public void setMaxHi(Short maxHi) {
        this.maxHi = maxHi;
    }

    public Short getMinLo() {
        return minLo;
    }

    public void setMinLo(Short minLo) {
        this.minLo = minLo;
    }

    public Short getMinHi() {
        return minHi;
    }

    public void setMinHi(Short minHi) {
        this.minHi = minHi;
    }

    public Short getRangeLo() {
        return rangeLo;
    }

    public void setRangeLo(Short rangeLo) {
        this.rangeLo = rangeLo;
    }

    public Short getRangeHi() {
        return rangeHi;
    }

    public void setRangeHi(Short rangeHi) {
        this.rangeHi = rangeHi;
    }

    public Short getCogMidMax() {
        return cogMidMax;
    }

    public void setCogMidMax(Short cogMidMax) {
        this.cogMidMax = cogMidMax;
    }

    public Short getCogMidRange() {
        return cogMidRange;
    }

    public void setCogMidRange(Short cogMidRange) {
        this.cogMidRange = cogMidRange;
    }

    public TaxaSpecies getSpecies() {
        return species;
    }

    public void setSpecies(TaxaSpecies species) {
        this.species = species;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof MCRSummary)) return false;

        MCRSummary that = (MCRSummary) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (maxLo != null ? !maxLo.equals(that.maxLo) : that.maxLo != null) return false;
        if (maxHi != null ? !maxHi.equals(that.maxHi) : that.maxHi != null) return false;
        if (minLo != null ? !minLo.equals(that.minLo) : that.minLo != null) return false;
        if (minHi != null ? !minHi.equals(that.minHi) : that.minHi != null) return false;
        if (rangeLo != null ? !rangeLo.equals(that.rangeLo) : that.rangeLo != null) return false;
        if (rangeHi != null ? !rangeHi.equals(that.rangeHi) : that.rangeHi != null) return false;
        if (cogMidMax != null ? !cogMidMax.equals(that.cogMidMax) : that.cogMidMax != null) return false;
        if (cogMidRange != null ? !cogMidRange.equals(that.cogMidRange) : that.cogMidRange != null) return false;
        return species.equals(that.species);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (maxLo != null ? maxLo.hashCode() : 0);
        result = 31 * result + (maxHi != null ? maxHi.hashCode() : 0);
        result = 31 * result + (minLo != null ? minLo.hashCode() : 0);
        result = 31 * result + (minHi != null ? minHi.hashCode() : 0);
        result = 31 * result + (rangeLo != null ? rangeLo.hashCode() : 0);
        result = 31 * result + (rangeHi != null ? rangeHi.hashCode() : 0);
        result = 31 * result + (cogMidMax != null ? cogMidMax.hashCode() : 0);
        result = 31 * result + (cogMidRange != null ? cogMidRange.hashCode() : 0);
        result = 31 * result + species.hashCode();
        return result;
    }
}
