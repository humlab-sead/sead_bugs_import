package se.sead.bugsimport.locations.country;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.sead.bugsimport.Importer;
import se.sead.bugsimport.locations.country.bugsmodel.Country;
import se.sead.bugsimport.locations.seadmodel.Location;

@Service
public class CountryImporter extends Importer<Country, Location> {

    @Autowired
    public CountryImporter(
            CountryToLocationMapper dataMapper,
            CountryPersister persister) {
        super(dataMapper, persister);
    }
}
