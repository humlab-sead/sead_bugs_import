package se.sead.bugsimport.specieskeys.seadmodel;

import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.bugsimport.bibliography.seadmodel.Biblio;
import se.sead.sead.model.LoggableEntity;

import javax.persistence.*;

@Entity
@Table(name="tbl_text_identification_keys")
@SequenceGenerator(name="id_keys_seq", sequenceName = "tbl_text_identification_keys_key_id_seq")
public class TextIdentificationKeys extends LoggableEntity{

    @Id
    @GeneratedValue(generator = "id_keys_seq", strategy = GenerationType.IDENTITY)
    @Column(name="key_id")
    private Integer id;
    @Column(name="key_text")
    private String keys;
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

    public String getKeys() {
        return keys;
    }

    public void setKeys(String keys) {
        this.keys = keys;
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
        if (o == null || !(o instanceof TextIdentificationKeys)) return false;

        TextIdentificationKeys that = (TextIdentificationKeys) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (keys != null ? !keys.equals(that.keys) : that.keys != null) return false;
        if (reference != null ? !reference.equals(that.reference) : that.reference != null) return false;
        return species != null ? species.equals(that.species) : that.species == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (keys != null ? keys.hashCode() : 0);
        result = 31 * result + (reference != null ? reference.hashCode() : 0);
        result = 31 * result + (species != null ? species.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TextIdentificationKeys{" +
                "id=" + id +
                ", keys='" + keys + '\'' +
                ", reference=" + reference +
                ", species=" + species +
                '}';
    }
}
