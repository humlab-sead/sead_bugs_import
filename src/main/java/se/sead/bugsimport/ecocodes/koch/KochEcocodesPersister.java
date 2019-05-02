package se.sead.bugsimport.ecocodes.koch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.Persister;
import se.sead.bugsimport.ecocodes.koch.bugsmodel.EcoKoch;
import se.sead.bugsimport.ecocodes.seadmodel.Ecocode;
import se.sead.repositories.EcocodeRepository;

@Component
public class KochEcocodesPersister extends Persister<EcoKoch, Ecocode> {

    @Autowired
    private EcocodeRepository repository;

    @Override
    protected Ecocode save(Ecocode seadValue) {
        return repository.saveOrUpdate(seadValue);
    }
}
