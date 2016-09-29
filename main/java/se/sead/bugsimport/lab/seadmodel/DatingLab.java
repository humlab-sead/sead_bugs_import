package se.sead.bugsimport.lab.seadmodel;

import se.sead.bugsimport.locations.seadmodel.Location;
import se.sead.sead.model.LoggableEntity;

import javax.persistence.*;

@Entity
@Table(name = "tbl_dating_labs")
@SequenceGenerator(name = "dating_lab_id_gen", sequenceName = "tbl_dating_labs_dating_lab_id_seq")
public class DatingLab extends LoggableEntity{

    @Id
    @GeneratedValue(generator = "dating_lab_id_gen", strategy = GenerationType.IDENTITY)
    @Column(name="dating_lab_id", nullable = false)
    private Integer id;
    @Column(name = "international_lab_id")
    private String labId;
    @Column(name = "lab_name")
    private String name;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "country_id")
    private Location country;

    @Override
    public Integer getId() {
        return id;
    }

    protected void setId(Integer id) {
        this.id = id;
    }

    public String getLabId() {
        return labId;
    }

    public void setLabId(String labId) {
        this.labId = labId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getCountry() {
        return country;
    }

    public void setCountry(Location country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof DatingLab)) return false;

        DatingLab datingLab = (DatingLab) o;

        if (id != null ? !id.equals(datingLab.id) : datingLab.id != null) return false;
        if (labId != null ? !labId.equals(datingLab.labId) : datingLab.labId != null) return false;
        if (name != null ? !name.equals(datingLab.name) : datingLab.name != null) return false;
        return country != null ? country.equals(datingLab.country) : datingLab.country == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (labId != null ? labId.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DatingLab{" +
                "id=" + id +
                ", labId='" + labId + '\'' +
                ", name='" + name + '\'' +
                ", country=" + country +
                '}';
    }
}
