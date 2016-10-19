package se.sead.bugsimport.fossil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.Persister;
import se.sead.bugsimport.fossil.bugsmodel.Fossil;
import se.sead.bugsimport.fossil.converters.AnalysisEntityManager;
import se.sead.bugsimport.fossil.seadmodel.Abundance;
import se.sead.repositories.AbundanceRepository;
import se.sead.sead.data.AnalysisEntity;

import java.util.List;

@Component
public class FossilPersister extends Persister<Fossil, Abundance> {

    @Autowired
    private AbundanceRepository repository;
    @Autowired
    private AnalysisEntityManager analysisEntityManager;

    @Override
    protected Abundance save(Abundance seadValue) {
        AnalysisEntity oldAnalysisEntity = seadValue.getAnalysisEntity();
        Abundance storedAbundances = repository.saveOrUpdate(seadValue);
        reassociate(oldAnalysisEntity, storedAbundances.getAnalysisEntity());
        return storedAbundances;
    }

    private void reassociate(AnalysisEntity originalAnalysisEntity, AnalysisEntity analysisEntity){
        if(originalAnalysisEntity.isNewItem()) {
            List<Abundance> abundancesFor = analysisEntityManager.getAbundancesFor(originalAnalysisEntity);
            abundancesFor.forEach(abundance -> abundance.setAnalysisEntity(analysisEntity));
        }
    }
}
