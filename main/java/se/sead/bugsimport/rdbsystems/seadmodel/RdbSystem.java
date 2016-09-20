package se.sead.bugsimport.rdbsystems.seadmodel;

import se.sead.bugsimport.locations.seadmodel.Location;
import se.sead.sead.model.Biblio;
import se.sead.sead.model.LoggableEntity;

import javax.persistence.*;

@Entity
@Table(name = "tbl_rdb_systems")
@SequenceGenerator(name = "rdb_systems_id_gen", sequenceName = "tbl_rdb_systems_rdb_system_id_seq")
public class RdbSystem extends LoggableEntity {

    @Id
    @GeneratedValue(generator = "rdb_systems_id_gen", strategy = GenerationType.IDENTITY)
    @Column(name = "rdb_system_id", nullable = false)
    private Integer id;
    @Column(name = "rdb_first_published")
    private Short firstPublished;
    @Column(name = "rdb_system")
    private String systemName;
    @Column(name = "rdb_system_date")
    private Integer systemDate;
    @Column(name = "rdb_version")
    private String version;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "biblio_id")
    private Biblio reference;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "location_id")
    private Location location;

    @Override
    public Integer getId() {
        return id;
    }

    protected void setId(Integer id){
        this.id = id;
    }

    public Short getFirstPublished() {
        return firstPublished;
    }

    public void setFirstPublished(Short firstPublished) {
        this.firstPublished = firstPublished;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public Integer getSystemDate() {
        return systemDate;
    }

    public void setSystemDate(Integer systemDate) {
        this.systemDate = systemDate;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Biblio getReference() {
        return reference;
    }

    public void setReference(Biblio reference) {
        this.reference = reference;
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
        if (o == null || !(o instanceof RdbSystem)) return false;

        RdbSystem rdbSystem = (RdbSystem) o;

        if (id != null ? !id.equals(rdbSystem.id) : rdbSystem.id != null) return false;
        if (firstPublished != null ? !firstPublished.equals(rdbSystem.firstPublished) : rdbSystem.firstPublished != null)
            return false;
        if (systemName != null ? !systemName.equals(rdbSystem.systemName) : rdbSystem.systemName != null) return false;
        if (systemDate != null ? !systemDate.equals(rdbSystem.systemDate) : rdbSystem.systemDate != null) return false;
        if (version != null ? !version.equals(rdbSystem.version) : rdbSystem.version != null) return false;
        if (reference != null ? !reference.equals(rdbSystem.reference) : rdbSystem.reference != null) return false;
        return location != null ? location.equals(rdbSystem.location) : rdbSystem.location == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (firstPublished != null ? firstPublished.hashCode() : 0);
        result = 31 * result + (systemName != null ? systemName.hashCode() : 0);
        result = 31 * result + (systemDate != null ? systemDate.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (reference != null ? reference.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RdbSystem{" +
                "id=" + id +
                ", systemName='" + systemName + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
