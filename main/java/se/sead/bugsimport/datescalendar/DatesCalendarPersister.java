package se.sead.bugsimport.datescalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.Persister;
import se.sead.bugsimport.datescalendar.bugsmodel.DatesCalendar;
import se.sead.bugsimport.datesperiod.seadmodel.RelativeDate;
import se.sead.repositories.RelativeDateRepository;

@Component
public class DatesCalendarPersister extends Persister<DatesCalendar, RelativeDate> {

    @Autowired
    private RelativeDateRepository relativeDateRepository;

    @Override
    protected RelativeDate save(RelativeDate seadValue) {
        return relativeDateRepository.saveOrUpdate(seadValue);
    }
}
