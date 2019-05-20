package se.sead.bugsimport.fossil.seadmodel;

import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.sead.data.AnalysisEntity;
import se.sead.sead.model.LoggableEntity;

import javax.persistence.*;

@Entity
@Table(name = "tbl_abundances")
@SequenceGenerator(name = "abundance_id_gen", sequenceName = "tbl_abundances_abundance_id_seq")
public class Abundance extends LoggableEntity {

    @Id
    @GeneratedValue(generator = "abundance_id_gen", strategy = GenerationType.IDENTITY)
    @Column(name = "abundance_id", nullable = false)
    private Integer id;
    @Column(name = "abundance")
    private Integer abundance;
    @Column(name = "abundance_element_id")
    private Integer abundanceElementId = 5; // always "MNI"
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "analysis_entity_id")
    private AnalysisEntity analysisEntity;
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

    public Integer getAbundance() {
        return abundance;
    }

    public void setAbundance(Integer abundance) {
        this.abundance = abundance;
    }

    public AnalysisEntity getAnalysisEntity() {
        return analysisEntity;
    }

    public void setAnalysisEntity(AnalysisEntity analysisEntity) {
        this.analysisEntity = analysisEntity;
    }

    public TaxaSpecies getSpecies() {
        return species;
    }

    public void setSpecies(TaxaSpecies species) {
        this.species = species;
    }

    public Integer getAbundanceElementId() {
        return abundanceElementId;
    }

    public void setAbundanceElementId(int abundanceElementId ) {
        // noop, always 5
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Abundance)) return false;

        Abundance abundance1 = (Abundance) o;

        if (id != null ? !id.equals(abundance1.id) : abundance1.id != null) return false;
        if (abundance != null ? !abundance.equals(abundance1.abundance) : abundance1.abundance != null) return false;
        if (analysisEntity != null ? !analysisEntity.equals(abundance1.analysisEntity) : abundance1.analysisEntity != null)
            return false;
        return species != null ? species.equals(abundance1.species) : abundance1.species == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (abundance != null ? abundance.hashCode() : 0);
        result = 31 * result + (analysisEntity != null ? analysisEntity.hashCode() : 0);
        result = 31 * result + (species != null ? species.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Abundance{" +
                "id=" + id +
                ", abundance=" + abundance +
                ", analysisEntity=" + analysisEntity +
                ", species=" + species +
                '}';
    }
}
