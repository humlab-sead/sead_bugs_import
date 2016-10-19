package se.sead.bugsimport.periods.seadmodel;

import se.sead.bugsimport.locations.seadmodel.Location;
import se.sead.sead.model.LoggableEntity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "tbl_relative_ages")
@SequenceGenerator(name = "relative_age_id_gen", sequenceName = "tbl_relative_ages_relative_age_id_seq")
public class RelativeAge extends LoggableEntity {

    @Id
    @GeneratedValue(generator = "relative_age_id_gen", strategy = GenerationType.IDENTITY)
    @Column(name = "relative_age_id", nullable = false)
    private Integer id;
    @Column(name = "relative_age_name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "c14_age_older", precision = 20, scale=5)
    private BigDecimal c14AgeOlder;
    @Column(name = "c14_age_younger", precision = 20, scale=5)
    private BigDecimal c14AgeYounger;
    @Column(name = "cal_age_older", precision = 20, scale=5)
    private BigDecimal calAgeOlder;
    @Column(name = "cal_age_younger", precision = 20, scale=5)
    private BigDecimal calAgeYounger;
    @Column(name = "notes")
    private String notes;
    @Column(name = "Abbreviation")
    private String abbreviation;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "relative_age_type_id")
    private RelativeAgeType type;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "location_id")
    private Location geographicExtent;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getC14AgeOlder() {
        return c14AgeOlder;
    }

    public void setC14AgeOlder(BigDecimal c14AgeOlder) {
        this.c14AgeOlder = c14AgeOlder;
    }

    public BigDecimal getC14AgeYounger() {
        return c14AgeYounger;
    }

    public void setC14AgeYounger(BigDecimal c14AgeYounger) {
        this.c14AgeYounger = c14AgeYounger;
    }

    public BigDecimal getCalAgeOlder() {
        return calAgeOlder;
    }

    public void setCalAgeOlder(BigDecimal calAgeOlder) {
        this.calAgeOlder = calAgeOlder;
    }

    public BigDecimal getCalAgeYounger() {
        return calAgeYounger;
    }

    public void setCalAgeYounger(BigDecimal calAgeYounger) {
        this.calAgeYounger = calAgeYounger;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public RelativeAgeType getType() {
        return type;
    }

    public void setType(RelativeAgeType type) {
        this.type = type;
    }

    public Location getGeographicExtent() {
        return geographicExtent;
    }

    public void setGeographicExtent(Location geographicExtent) {
        this.geographicExtent = geographicExtent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RelativeAge that = (RelativeAge) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (c14AgeOlder != null ? !c14AgeOlder.equals(that.c14AgeOlder) : that.c14AgeOlder != null) return false;
        if (c14AgeYounger != null ? !c14AgeYounger.equals(that.c14AgeYounger) : that.c14AgeYounger != null)
            return false;
        if (calAgeOlder != null ? !calAgeOlder.equals(that.calAgeOlder) : that.calAgeOlder != null) return false;
        if (calAgeYounger != null ? !calAgeYounger.equals(that.calAgeYounger) : that.calAgeYounger != null)
            return false;
        if (notes != null ? !notes.equals(that.notes) : that.notes != null) return false;
        if (abbreviation != null ? !abbreviation.equals(that.abbreviation) : that.abbreviation != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        return geographicExtent != null ? geographicExtent.equals(that.geographicExtent) : that.geographicExtent == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (c14AgeOlder != null ? c14AgeOlder.hashCode() : 0);
        result = 31 * result + (c14AgeYounger != null ? c14AgeYounger.hashCode() : 0);
        result = 31 * result + (calAgeOlder != null ? calAgeOlder.hashCode() : 0);
        result = 31 * result + (calAgeYounger != null ? calAgeYounger.hashCode() : 0);
        result = 31 * result + (notes != null ? notes.hashCode() : 0);
        result = 31 * result + (abbreviation != null ? abbreviation.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (geographicExtent != null ? geographicExtent.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RelativeAge{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", c14AgeOlder=" + c14AgeOlder +
                ", c14AgeYounger=" + c14AgeYounger +
                ", calAgeOlder=" + calAgeOlder +
                ", calAgeYounger=" + calAgeYounger +
                ", notes='" + notes + '\'' +
                ", abbreviation='" + abbreviation + '\'' +
                ", type=" + type +
                ", geographicExtent=" + geographicExtent +
                '}';
    }
}
