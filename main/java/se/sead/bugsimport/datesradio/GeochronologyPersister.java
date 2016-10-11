package se.sead.bugsimport.datesradio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.Persister;
import se.sead.bugsimport.datesradio.bugsmodel.DatesRadio;
import se.sead.bugsimport.datesradio.seadmodel.Geochronology;
import se.sead.repositories.GeochronologyRepository;

@Component
public class GeochronologyPersister extends Persister<DatesRadio, Geochronology> {

    @Autowired
    private GeochronologyRepository repository;

    @Override
    protected Geochronology save(Geochronology seadValue) {
        return repository.saveOrUpdate(seadValue);
    }
}
