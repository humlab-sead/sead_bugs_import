package se.sead.bugsimport.datescalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.Persister;
import se.sead.bugsimport.datescalendar.bugsmodel.DatesCalendar;
import se.sead.bugsimport.datesperiod.seadmodel.RelativeDate;
import se.sead.bugsimport.periods.converters.RelativeAgeCache;
import se.sead.bugsimport.periods.seadmodel.RelativeAge;
import se.sead.repositories.RelativeDateRepository;

@Component
public class DatesCalendarPersister extends Persister<DatesCalendar, RelativeDate> {

    @Autowired
    private RelativeDateRepository relativeDateRepository;
    @Autowired
    private RelativeAgeCache relativeAgeCache;

    @Override
    protected RelativeDate save(RelativeDate seadValue) {
        boolean cachedRelativeAgeIsNew = updateStaleRelativeAgeInstances(seadValue);
        RelativeDate relativeDate = relativeDateRepository.saveOrUpdate(seadValue);
        if(cachedRelativeAgeIsNew){
            updateCacheWithStoredRelativeAge(relativeDate);
        }
        return relativeDate;
    }

    private boolean updateStaleRelativeAgeInstances(RelativeDate seadValue) {
        boolean reassociateRelativeAge = false;
        if(seadValue.getRelativeAge().isNewItem()){
            RelativeAge fromCache = relativeAgeCache.get(seadValue.getRelativeAge().getAbbreviation());
            reassociateRelativeAge = fromCache.isNewItem();
            seadValue.setRelativeAge(fromCache);
        }
        return reassociateRelativeAge;
    }

    private void updateCacheWithStoredRelativeAge(RelativeDate relativeDate) {
        relativeAgeCache.put(relativeDate.getRelativeAge());
    }
}
