package se.sead.bugsimport.datesperiod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.Persister;
import se.sead.bugsimport.datesperiod.bugsmodel.DatesPeriod;
import se.sead.bugsimport.datesperiod.seadmodel.RelativeDate;
import se.sead.repositories.RelativeDateRepository;

@Component
public class DatesPeriodPersister extends Persister<DatesPeriod, RelativeDate> {

    @Autowired
    private RelativeDateRepository repository;

    @Override
    protected RelativeDate save(RelativeDate seadValue) {
        return repository.saveOrUpdate(seadValue);
    }
}
