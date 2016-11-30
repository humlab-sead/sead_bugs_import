package se.sead.bugsimport.rdb.seadmodel;

import se.sead.bugsimport.locations.seadmodel.Location;
import se.sead.bugsimport.rdbcodes.seadmodel.RdbCode;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.sead.model.LoggableEntity;

import javax.persistence.*;

@Entity
@Table(name = "tbl_rdb")
@SequenceGenerator(name = "rdb_id_gen", sequenceName = "tbl_rdb_rdb_id_seq")
public class Rdb extends LoggableEntity{

    @Id
    @GeneratedValue(generator = "rdb_id_gen", strategy = GenerationType.IDENTITY)
    @Column(name = "rdb_id", nullable = false)
    private Integer id;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "rdb_code_id")
    private RdbCode rdbCode;
    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location country;
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

    public RdbCode getRdbCode() {
        return rdbCode;
    }

    public void setRdbCode(RdbCode rdbCode) {
        this.rdbCode = rdbCode;
    }

    public Location getCountry() {
        return country;
    }

    public void setCountry(Location country) {
        this.country = country;
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
        if (o == null || !(o instanceof Rdb)) return false;

        Rdb rdb = (Rdb) o;

        if (id != null ? !id.equals(rdb.id) : rdb.id != null) return false;
        if (rdbCode != null ? !rdbCode.equals(rdb.rdbCode) : rdb.rdbCode != null) return false;
        if (country != null ? !country.equals(rdb.country) : rdb.country != null) return false;
        return species != null ? species.equals(rdb.species) : rdb.species == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (rdbCode != null ? rdbCode.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (species != null ? species.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Rdb{" +
                "id=" + id +
                ", rdbCode=" + rdbCode +
                ", country=" + country +
                ", species=" + species +
                '}';
    }
}
