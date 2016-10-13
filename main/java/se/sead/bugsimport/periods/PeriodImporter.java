package se.sead.bugsimport.periods;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.sead.bugsimport.BugsSeadMapper;
import se.sead.bugsimport.Importer;
import se.sead.bugsimport.Persister;
import se.sead.bugsimport.locations.country.CountryImporter;
import se.sead.bugsimport.periods.bugsmodel.Period;
import se.sead.bugsimport.periods.seadmodel.RelativeAge;

@Service
public class PeriodImporter extends Importer<Period, RelativeAge> {

    @Autowired
    public PeriodImporter(
            PeriodBugsSeadMapper dataMapper,
            PeriodPersister persister,
            CountryImporter countryImporter) {
        super(dataMapper, persister, countryImporter);
    }
}
