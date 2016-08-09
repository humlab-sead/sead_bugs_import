package se.sead.bugsimport.country;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.Persister;
import se.sead.bugsimport.country.bugsmodel.Country;
import se.sead.bugsimport.country.seadmodel.Location;
import se.sead.bugsimport.tracing.TracePersister;
import se.sead.repositories.LocationRepository;

@Component
public class CountryPersister extends Persister<Country, Location> {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    public CountryPersister(TracePersister tracePersister){
        super(tracePersister);
    }

    @Override
    protected Location save(Location seadValue) {
        return locationRepository.saveOrUpdate(seadValue);
    }
}
