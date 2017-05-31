package se.sead.bugsimport.species.seadmodel;

import se.sead.sead.model.LoggableEntity;

import javax.persistence.*;

@Entity
@Table(name = "tbl_taxa_tree_master")
@SequenceGenerator(name = "species_id_seq", sequenceName = "tbl_taxa_tree_master_taxon_id_seq")
public class TaxaSpecies extends LoggableEntity {

    @Id
    @GeneratedValue(generator = "species_id_seq", strategy = GenerationType.IDENTITY)
    @Column(name = "taxon_id", nullable = false)
    private Integer id;
    @Column(name = "species")
    private String speciesName;
    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "genus_id")
    private TaxaGenus genus;
    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "author_id")
    private TaxaAuthor author;

    public Integer getId() {
        return id;
    }

    protected void setId(Integer id) {
        this.id = id;
    }

    public String getSpeciesName() {
        return speciesName;
    }

    public void setSpeciesName(String speciesName) {
        this.speciesName = speciesName;
    }

    public TaxaGenus getGenus() {
        return genus;
    }

    public void setGenus(TaxaGenus genus) {
        this.genus = genus;
    }

    public TaxaAuthor getAuthor() {
        return author;
    }

    public void setAuthor(TaxaAuthor author) {
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof TaxaSpecies)) return false;

        TaxaSpecies that = (TaxaSpecies) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (speciesName != null ? !speciesName.equals(that.speciesName) : that.speciesName != null) return false;
        if (genus != null ? !genus.equals(that.genus) : that.genus != null) return false;
        return author != null ? author.equals(that.author) : that.author == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (speciesName != null ? speciesName.hashCode() : 0);
        result = 31 * result + (genus != null ? genus.hashCode() : 0);
        result = 31 * result + (author != null ? author.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        String species = "";
        TaxaGenus genus = getGenus();
        if(genus != null){
            TaxaFamily family = genus.getFamily();
            if (family != null) {
                species = family.getFamilyName();
            }
            species += " " + genus.getGenusName();
        }
        species += " " + speciesName;
        return
                "Species{" +
                        (getId() != null ? getId() : "") +
                        "," +
                        species + " " +
                        (getAuthor() != null ? getAuthor().getAuthorName() : "") +
                        "}";
    }
}
