package se.sead.bugsimport.datesperiod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.sead.bugsimport.Importer;
import se.sead.bugsimport.datesperiod.bugsmodel.DatesPeriod;
import se.sead.bugsimport.datesperiod.seadmodel.RelativeDate;
import se.sead.bugsimport.periods.PeriodImporter;
import se.sead.bugsimport.sample.SampleImporter;

@Service
public class DatesPeriodImporter extends Importer<DatesPeriod, RelativeDate> {

    @Autowired
    public DatesPeriodImporter(
            DatesPeriodBugsSeadMapper dataMapper,
            DatesPeriodPersister persister,
            PeriodImporter periodImporter,
            SampleImporter sampleImporter) {
        super(dataMapper, persister, sampleImporter, periodImporter);
    }
}
