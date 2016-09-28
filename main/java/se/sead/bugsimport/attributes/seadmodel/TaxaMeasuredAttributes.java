package se.sead.bugsimport.attributes.seadmodel;

import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.sead.model.LoggableEntity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "tbl_taxa_measured_attributes")
@SequenceGenerator(name = "measured_attributes_id_gen", sequenceName = "tbl_taxa_measured_attributes_measured_attribute_id_seq")
public class TaxaMeasuredAttributes extends LoggableEntity {

    @Id
    @GeneratedValue(generator = "measured_attributes_id_gen", strategy = GenerationType.IDENTITY)
    @Column(name = "measured_attribute_id", nullable = false)
    private Integer id;
    @Column(name = "attribute_measure")
    private String measure;
    @Column(name = "attribute_type")
    private String type;
    @Column(name = "attribute_units")
    private String units;
    @Column(name = "data", precision = 18, scale = 10)
    private BigDecimal value;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "taxon_id")
    private TaxaSpecies species;

    @Override
    public Integer getId() {
        return id;
    }

    protected void setId(Integer id) {
        this.id = id;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
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
        if (o == null || !(o instanceof TaxaMeasuredAttributes)) return false;

        TaxaMeasuredAttributes that = (TaxaMeasuredAttributes) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (measure != null ? !measure.equals(that.measure) : that.measure != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (units != null ? !units.equals(that.units) : that.units != null) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;
        return species != null ? species.equals(that.species) : that.species == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (measure != null ? measure.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (units != null ? units.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (species != null ? species.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TaxaMeasuredAttributes{" +
                "id=" + id +
                ", measure='" + measure + '\'' +
                ", type='" + type + '\'' +
                ", units='" + units + '\'' +
                ", value=" + value +
                ", species=" + species +
                '}';
    }
}
