package se.sead.bugsimport.ecocodes.seadmodel;

import se.sead.bugsimport.ecocodedefinition.seadmodel.EcocodeDefinition;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.sead.model.LoggableEntity;

import javax.persistence.*;

@Entity
@Table(name = "tbl_ecocodes")
@SequenceGenerator(name = "ecocode_id_gen", sequenceName = "tbl_ecocodes_ecocode_id_seq")
public class Ecocode extends LoggableEntity{

    @Id
    @GeneratedValue(generator = "ecocode_id_gen", strategy = GenerationType.IDENTITY)
    @Column(name = "ecocode_id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ecocode_definition_id")
    private EcocodeDefinition ecocodeDefinition;

    @ManyToOne
    @JoinColumn(name = "taxon_id")
    private TaxaSpecies species;

    @Override
    public Integer getId() {
        return id;
    }

    protected void setId(Integer id) {
        this.id = id;
    }

    public EcocodeDefinition getEcocodeDefinition() {
        return ecocodeDefinition;
    }

    public void setEcocodeDefinition(EcocodeDefinition ecocodeDefinition) {
        this.ecocodeDefinition = ecocodeDefinition;
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
        if (o == null || !(o instanceof Ecocode)) return false;

        Ecocode ecocode = (Ecocode) o;

        if (id != null ? !id.equals(ecocode.id) : ecocode.id != null) return false;
        if (ecocodeDefinition != null ? !ecocodeDefinition.equals(ecocode.ecocodeDefinition) : ecocode.ecocodeDefinition != null)
            return false;
        return species != null ? species.equals(ecocode.species) : ecocode.species == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (ecocodeDefinition != null ? ecocodeDefinition.hashCode() : 0);
        result = 31 * result + (species != null ? species.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Ecocode{" +
                "id=" + id +
                ", ecocodeDefinition=" + ecocodeDefinition +
                ", species=" + species +
                '}';
    }
}
