package se.sead.bugsimport.taxaseasonality;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.Persister;
import se.sead.bugsimport.taxaseasonality.bugsmodel.SeasonActiveAdult;
import se.sead.bugsimport.taxaseasonality.seadmodel.TaxaSeasonality;
import se.sead.repositories.TaxaSeasonalityRepository;

@Component
public class TaxaSeasonalityPersister extends Persister<SeasonActiveAdult, TaxaSeasonality> {

    @Autowired
    private TaxaSeasonalityRepository repository;

    @Override
    protected TaxaSeasonality save(TaxaSeasonality seadValue) {
        return repository.saveOrUpdate(seadValue);
    }
}
