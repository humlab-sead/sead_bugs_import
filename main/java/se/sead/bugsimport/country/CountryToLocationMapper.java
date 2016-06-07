package se.sead.bugsimport.country;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugs.AccessReaderProvider;
import se.sead.bugsimport.BugsSeadMapper;
import se.sead.bugsimport.BugsTableRowConverter;
import se.sead.bugsimport.country.bugsmodel.Country;
import se.sead.bugsimport.country.bugsmodel.CountryBugsTable;
import se.sead.bugsimport.country.seadmodel.Location;

@Component
public class CountryToLocationMapper extends BugsSeadMapper<Country, Location> {

    @Autowired
    public CountryToLocationMapper(
            AccessReaderProvider readerProvider,
            CountryRowConverter countryRowConverter
    ){
        super(readerProvider.getReader(), new CountryBugsTable(), countryRowConverter);
    }
}
