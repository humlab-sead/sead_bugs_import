package se.sead.bugsimport.species.seadmodel;

import se.sead.sead.model.LoggableEntity;

import javax.persistence.*;

@Entity
@Table(name = "tbl_taxa_tree_master")
@SequenceGenerator(name = "species_id_seq", sequenceName = "tbl_taxa_tree_master_taxon_id_seq")
public class TaxaSpecies extends LoggableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "species_id_seq")
    @Column(name = "taxon_id", nullable = false)
    private Integer id;
    @Column(name = "species")
    private String speciesName;
    //@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "genus_id")
    private TaxaGenus genus;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "author_id")
    private TaxaAuthor taxaAuthor;

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

    public TaxaAuthor getTaxaAuthor() {
        return taxaAuthor;
    }

    public void setTaxaAuthor(TaxaAuthor taxaAuthor) {
        this.taxaAuthor = taxaAuthor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof TaxaSpecies)) return false;

        TaxaSpecies that = (TaxaSpecies) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (!speciesName.equals(that.speciesName)) return false;
        if (!genus.equals(that.genus)) return false;
        return taxaAuthor != null ? taxaAuthor.equals(that.taxaAuthor) : that.taxaAuthor == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + speciesName.hashCode();
        result = 31 * result + genus.hashCode();
        result = 31 * result + (taxaAuthor != null ? taxaAuthor.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return
                "Species{" +
                        (getId() != null ? getId() : "") +
                        "," +
                        getGenus().getTaxaFamily().getFamilyName() +
                        " " +
                        getGenus().getGenusName() +
                        " " +
                        speciesName +
                        " " +
                        (getTaxaAuthor() != null ? getTaxaAuthor().getAuthorName() : "") +
                        "}";
    }
}
