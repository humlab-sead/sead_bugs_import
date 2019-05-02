package se.sead.bugsimport.speciesbiology.seadmodel;

import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.bugsimport.bibliography.seadmodel.Biblio;
import se.sead.sead.model.LoggableEntity;

import javax.persistence.*;

/**
 * Created by erer0001 on 2016-05-18.
 */
@Entity
@Table(name="tbl_text_biology")
@SequenceGenerator(name = "text_biology_seq", sequenceName = "tbl_text_biology_biology_id_seq")
public class TextBiology extends LoggableEntity {

    @Id
    @GeneratedValue(generator = "text_biology_seq", strategy = GenerationType.IDENTITY)
    @Column(name="biology_id")
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "biblio_id")
    private Biblio reference;
    @Column(name="biology_text", nullable = false)
    private String text;
    @ManyToOne
    @JoinColumn(name="taxon_id", nullable = false)
    private TaxaSpecies species;

    @Override
    public Integer getId() {
        return id;
    }

    protected void setId(Integer id){
        this.id = id;
    }

    public Biblio getReference() {
        return reference;
    }

    public void setReference(Biblio reference) {
        this.reference = reference;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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
        if (o == null || !(o instanceof TextBiology)) return false;

        TextBiology that = (TextBiology) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (reference != null ? !reference.equals(that.reference) : that.reference != null) return false;
        if (text != null ? !text.equals(that.text) : that.text != null) return false;
        return species != null ? species.equals(that.species) : that.species == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (reference != null ? reference.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (species != null ? species.hashCode() : 0);
        return result;
    }

    @Override
    public String toString(){
        return "TextBiology{" +
                species + "," +
                reference + ","+
                text + "}";
    }
}
