package se.sead.bugsimport.site.seadmodel;

import se.sead.bugsimport.country.seadmodel.Location;
import se.sead.sead.model.LoggableEntity;

import javax.persistence.*;

@Entity
@Table(name="tbl_site_locations")
@SequenceGenerator(name="site_location_id_seq", sequenceName = "tbl_site_locations_site_location_id_seq")
public class SiteLocation extends LoggableEntity {

    @Id
    @GeneratedValue(generator = "site_location_id_seq", strategy = GenerationType.AUTO)
    @Column(name="site_location_id")
    private Integer id;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name="location_id")
    private Location location;
    @ManyToOne
    @JoinColumn(name="site_id")
    private SeadSite site;

    @Override
    public Integer getId() {
        return id;
    }

    protected void setId(Integer id) {
        this.id = id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public SeadSite getSite() {
        return site;
    }

    public void setSite(SeadSite site) {
        this.site = site;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof SiteLocation)) return false;

        SiteLocation that = (SiteLocation) o;

        if (id != null && that.id != null && !id.equals(that.id)) return false;
        if (location != null ? !location.equals(that.location) : that.location != null) return false;
        return site != null ? site.equals(that.site) : that.site == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (site != null ? site.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SiteLocation{" +
                "id=" + id +
                ", location=" + location +
                ", site=" + site +
                "}";
    }
}
