package se.sead.bugsimport.speciesdistribution.seadmodel;

import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.bugsimport.bibliography.seadmodel.Biblio;
import se.sead.sead.model.LoggableEntity;

import javax.persistence.*;

@Entity
@Table(name="tbl_text_distribution")
@SequenceGenerator(name="text_distribution_id_seq", sequenceName = "tbl_text_distribution_distribution_id_seq")
public class TextDistribution extends LoggableEntity {

    @Id
    @GeneratedValue(generator = "text_distribution_id_seq", strategy = GenerationType.IDENTITY)
    @Column(name="distribution_id", nullable = false)
    private Integer id;

    @Column(name="distribution_text")
    private String distribution;
    @ManyToOne
    @JoinColumn(name="biblio_id")
    private Biblio reference;
    @ManyToOne
    @JoinColumn(name="taxon_id")
    private TaxaSpecies species;

    @Override
    public Integer getId() {
        return id;
    }

    protected void setId(Integer id){
        this.id = id;
    }

    public String getDistribution() {
        return distribution;
    }

    public void setDistribution(String distribution) {
        this.distribution = distribution;
    }

    public Biblio getReference() {
        return reference;
    }

    public void setReference(Biblio reference) {
        this.reference = reference;
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
        if (o == null || !(o instanceof TextDistribution)) return false;

        TextDistribution that = (TextDistribution) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (distribution != null ? !distribution.equals(that.distribution) : that.distribution != null) return false;
        if (reference != null ? !reference.equals(that.reference) : that.reference != null) return false;
        return species != null ? species.equals(that.species) : that.species == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (distribution != null ? distribution.hashCode() : 0);
        result = 31 * result + (reference != null ? reference.hashCode() : 0);
        result = 31 * result + (species != null ? species.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TextDistribution{" +
                "id=" + id +
                ", distribution='" + distribution + '\'' +
                ", reference=" + reference +
                ", species=" + species +
                '}';
    }
}
