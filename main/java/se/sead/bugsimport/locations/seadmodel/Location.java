package se.sead.bugsimport.locations.seadmodel;


import se.sead.sead.model.LoggableEntity;

import javax.persistence.*;

@Entity
@Table(name="tbl_locations")
@SequenceGenerator(name="location_id_seq", sequenceName = "tbl_locations_location_id_seq")
public class Location extends LoggableEntity implements Comparable<Location>{

    @Id
    @GeneratedValue(generator = "location_id_seq", strategy = GenerationType.IDENTITY)
    @Column(name = "location_id", nullable = false)
    private Integer id;
    @Column(name="location_name", nullable = false)
    private String name;
    @ManyToOne
    @JoinColumn(name="location_type_id")
    private LocationType type;

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

    public LocationType getType() {
        return type;
    }

    public void setType(LocationType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Location)) return false;

        Location location = (Location) o;

        if (id != null ? !id.equals(location.id) : location.id != null) return false;
        if (!name.equals(location.name)) return false;
        return type.equals(location.type);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + name.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }

    @Override
    public int compareTo(Location o) {
        return type.compareTo(o.getType()) +
               name.compareTo(o.getName());
    }

    @Override
    public String toString() {
        return "Location{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                '}';
    }
}
