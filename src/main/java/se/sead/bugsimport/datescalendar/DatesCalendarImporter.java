package se.sead.bugsimport.datescalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.sead.bugsimport.Importer;
import se.sead.bugsimport.datescalendar.bugsmodel.DatesCalendar;
import se.sead.bugsimport.datesperiod.seadmodel.RelativeDate;
import se.sead.bugsimport.periods.converters.RelativeAgeCache;

@Service
public class DatesCalendarImporter extends Importer<DatesCalendar, RelativeDate> {

    @Autowired
    private RelativeAgeCache relativeAgeCache;

    @Autowired
    public DatesCalendarImporter(
            DatesCalendarBugsSeadMapper dataMapper,
            DatesCalendarPersister persister) {
        super(dataMapper, persister);
    }

    @Override
    public void run() {
        super.run();
        relativeAgeCache.clear();
    }
}
