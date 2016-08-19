package se.sead.bugsimport.locations.country;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugs.AccessReaderProvider;
import se.sead.bugsimport.BugsSeadMapper;
import se.sead.bugsimport.locations.country.bugsmodel.Country;
import se.sead.bugsimport.locations.country.bugsmodel.CountryBugsTable;
import se.sead.bugsimport.locations.seadmodel.Location;
import se.sead.bugsimport.translations.BugsValueTranslationService;

@Component
public class CountryToLocationMapper extends BugsSeadMapper<Country, Location> {

    @Autowired
    public CountryToLocationMapper(
            AccessReaderProvider readerProvider,
            CountryRowConverter countryRowConverter,
            BugsValueTranslationService dataTranslationService
    ){
        super(new CountryBugsTable(), countryRowConverter);
    }
}
