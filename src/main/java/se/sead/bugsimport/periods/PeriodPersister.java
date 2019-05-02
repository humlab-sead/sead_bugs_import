package se.sead.bugsimport.periods;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.Persister;
import se.sead.bugsimport.periods.bugsmodel.Period;
import se.sead.bugsimport.periods.seadmodel.RelativeAge;
import se.sead.repositories.RelativeAgeRepository;

@Component
public class PeriodPersister extends Persister<Period, RelativeAge> {

    @Autowired
    private RelativeAgeRepository repository;

    @Override
    protected RelativeAge save(RelativeAge seadValue) {
        return repository.saveOrUpdate(seadValue);
    }
}
