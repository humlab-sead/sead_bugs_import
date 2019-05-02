package se.sead.bugsimport.taxaseasonality.seadmodel;

import se.sead.sead.model.LoggableEntity;

import javax.persistence.*;

@Entity
@Table(name="tbl_seasons")
@SequenceGenerator(name = "season_id_gen", sequenceName = "tbl_seasons_season_id_seq")
public class Season extends LoggableEntity {

    @Id
    @GeneratedValue(generator = "season_id_gen", strategy = GenerationType.IDENTITY)
    @Column(name = "season_id", nullable = false)
    private Integer id;

    @Column(name="season_name")
    private String name;
    @ManyToOne
    @JoinColumn(name = "season_type_id", nullable = false)
    private SeasonType type;
    @Column(name="sort_order")
    private Short sortOrder;

    @Override
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SeasonType getType() {
        return type;
    }

    public void setType(SeasonType type) {
        this.type = type;
    }

    public Short getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Short sortOrder) {
        this.sortOrder = sortOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Season)) return false;

        Season season = (Season) o;

        if (id != null && season.id != null && !id.equals(season.id)) return false;
        if (name != null ? !name.equals(season.name) : season.name != null) return false;
        if (type != null ? !type.equals(season.type) : season.type != null) return false;
        return sortOrder != null ? sortOrder.equals(season.sortOrder) : season.sortOrder == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (sortOrder != null ? sortOrder.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Season{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
