package se.sead.bugsimport.speciesdistribution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.Persister;
import se.sead.bugsimport.speciesdistribution.bugsmodel.Distrib;
import se.sead.bugsimport.speciesdistribution.seadmodel.TextDistribution;
import se.sead.repositories.TextDistributionRepository;

@Component
public class SpeciesDistributionPersister extends Persister<Distrib, TextDistribution> {

    @Autowired
    private TextDistributionRepository dataRepository;

    @Override
    protected TextDistribution save(TextDistribution seadValue) {
        return dataRepository.saveOrUpdate(seadValue);
    }
}
