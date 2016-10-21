package se.sead.bugsimport.speciesassociation.seadmodel;

import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.sead.model.Biblio;
import se.sead.sead.model.LoggableEntity;

import javax.persistence.*;

@Entity
@Table(name = "tbl_species_associations")
@SequenceGenerator(name = "species_associations_id_gen", sequenceName = "tbl_species_associations_species_association_id_seq")
public class SpeciesAssociation extends LoggableEntity {

    @Id
    @GeneratedValue(generator = "species_associations_id_gen", strategy = GenerationType.IDENTITY)
    @Column(name = "species_association_id", nullable = false)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "taxon_id")
    private TaxaSpecies sourceSpecies;
    @ManyToOne
    @JoinColumn(name = "associated_taxon_id")
    private TaxaSpecies targetSpecies;
    @ManyToOne
    @JoinColumn(name = "association_type_id")
    private SpeciesAssociationType type;
    @ManyToOne
    @JoinColumn(name = "biblio_id")
    private Biblio reference;

    @Override
    public Integer getId() {
        return id;
    }

    protected void setId(Integer id) {
        this.id = id;
    }

    public TaxaSpecies getSourceSpecies() {
        return sourceSpecies;
    }

    public void setSourceSpecies(TaxaSpecies sourceSpecies) {
        this.sourceSpecies = sourceSpecies;
    }

    public TaxaSpecies getTargetSpecies() {
        return targetSpecies;
    }

    public void setTargetSpecies(TaxaSpecies targetSpecies) {
        this.targetSpecies = targetSpecies;
    }

    public SpeciesAssociationType getType() {
        return type;
    }

    public void setType(SpeciesAssociationType type) {
        this.type = type;
    }

    public Biblio getReference() {
        return reference;
    }

    public void setReference(Biblio reference) {
        this.reference = reference;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof SpeciesAssociation)) return false;

        SpeciesAssociation that = (SpeciesAssociation) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (sourceSpecies != null ? !sourceSpecies.equals(that.sourceSpecies) : that.sourceSpecies != null)
            return false;
        if (targetSpecies != null ? !targetSpecies.equals(that.targetSpecies) : that.targetSpecies != null)
            return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        return reference != null ? reference.equals(that.reference) : that.reference == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (sourceSpecies != null ? sourceSpecies.hashCode() : 0);
        result = 31 * result + (targetSpecies != null ? targetSpecies.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (reference != null ? reference.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SpeciesAssociation{" +
                "id=" + id +
                ", sourceSpecies=" + sourceSpecies +
                ", targetSpecies=" + targetSpecies +
                ", type=" + type +
                ", reference=" + reference +
                '}';
    }
}
