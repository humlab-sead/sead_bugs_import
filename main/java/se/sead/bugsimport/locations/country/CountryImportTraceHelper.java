package se.sead.bugsimport.locations.country;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.locations.country.bugsmodel.Country;
import se.sead.bugsimport.locations.seadmodel.Location;
import se.sead.bugsimport.tracing.SeadDataFromTraceHelper;
import se.sead.repositories.LocationRepository;

@Component
public class CountryImportTraceHelper extends SeadDataFromTraceHelper<Country, Location> {

    @Autowired
    public CountryImportTraceHelper(LocationRepository locationRepository){
        super("TCountry", false, locationRepository);
    }
}
