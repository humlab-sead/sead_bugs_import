package se.sead.bugsimport.specieskeys.seadmodel;

import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.sead.model.Biblio;
import se.sead.sead.model.LoggableEntity;

import javax.persistence.*;

@Entity
@Table(name="tbl_text_identification_keys")
@SequenceGenerator(name="id_keys_seq", sequenceName = "tbl_text_identification_keys_key_id_seq")
public class TextIdentificationKeys extends LoggableEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "id_keys_seq")
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
        if (o == null || getClass() != o.getClass()) return false;

        TextIdentificationKeys that = (TextIdentificationKeys) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (keys != null ? !keys.equals(that.keys) : that.keys != null) return false;
        if (!reference.equals(that.reference)) return false;
        return species.equals(that.species);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (keys != null ? keys.hashCode() : 0);
        result = 31 * result + reference.hashCode();
        result = 31 * result + species.hashCode();
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
