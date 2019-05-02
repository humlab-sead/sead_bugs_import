package se.sead.model;

import se.sead.bugsimport.fossil.seadmodel.Abundance;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.sead.data.AnalysisEntity;

public class TestAbundance extends Abundance {

    private TestAbundance(Integer id){
        setId(id);
    }

    public static Abundance create(
            Integer id,
            TaxaSpecies species,
            AnalysisEntity analysisEntity,
            Integer abundance
    ){
        Abundance a = new TestAbundance(id);
        a.setAnalysisEntity(analysisEntity);
        a.setSpecies(species);
        a.setAbundance(abundance);
        return a;
    }
}
