package se.sead.bugsimport.taxaseasonality.seadmodel;

import se.sead.bugsimport.locations.seadmodel.Location;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.sead.model.LoggableEntity;

import javax.persistence.*;

@Entity
@Table(name="tbl_taxa_seasonality")
@SequenceGenerator(name = "taxa_seasonality_id_gen", sequenceName = "tbl_taxa_seasonality_seasonality_id_seq")
public class TaxaSeasonality extends LoggableEntity {

    @Id
    @GeneratedValue(generator = "taxa_seasonality_id_gen", strategy = GenerationType.IDENTITY)
    @Column(name = "seasonality_id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="activity_type_id")
    private ActivityType type;
    @ManyToOne
    @JoinColumn(name="season_id")
    private Season season;
    @ManyToOne
    @JoinColumn(name="taxon_id", nullable = false)
    private TaxaSpecies species;
    @ManyToOne
    @JoinColumn(name="location_id", nullable = false)
    private Location location;

    @Override
    public Integer getId() {
        return id;
    }

    protected void setId(Integer id){
        this.id = id;
    }

    public ActivityType getType() {
        return type;
    }

    public void setType(ActivityType type) {
        this.type = type;
    }

    public Season getSeason() {
        return season;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    public TaxaSpecies getSpecies() {
        return species;
    }

    public void setSpecies(TaxaSpecies species) {
        this.species = species;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof TaxaSeasonality)) return false;

        TaxaSeasonality that = (TaxaSeasonality) o;

        if (id != null && that.id != null && !id.equals(that.id)) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (season != null ? !season.equals(that.season) : that.season != null) return false;
        if (species != null ? !species.equals(that.species) : that.species != null) return false;
        return location != null ? location.equals(that.location) : that.location == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (season != null ? season.hashCode() : 0);
        result = 31 * result + (species != null ? species.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TaxaSeasonality{" +
                "id=" + id +
                ", type=" + type +
                ", season=" + season +
                ", location=" + location +
                ", species=" + species +
                '}';
    }
}
