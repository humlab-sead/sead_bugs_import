package se.sead.bugsimport.taxaseasonality.seadmodel;

import se.sead.sead.model.LoggableEntity;

import javax.persistence.*;

@Entity
@Table(name="tbl_season_types")
@SequenceGenerator(name="season_type_id_gen", sequenceName = "tbl_season_types_season_type_id_seq")
public class SeasonType extends LoggableEntity {

    @Id
    @GeneratedValue(generator = "season_type_id_gen", strategy = GenerationType.AUTO)
    @Column(name="season_type_id", nullable = false)
    private Integer id;

    @Column(name="season_type")
    private String seasonType;
    @Column(name="description")
    private String description;

    public String getSeasonType() {
        return seasonType;
    }

    public void setSeasonType(String seasonType) {
        this.seasonType = seasonType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof SeasonType)) return false;

        SeasonType that = (SeasonType) o;

        if (id != null && that.id != null && !id.equals(that.id)) return false;
        if (seasonType != null ? !seasonType.equals(that.seasonType) : that.seasonType != null) return false;
        return description != null ? description.equals(that.description) : that.description == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (seasonType != null ? seasonType.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}
