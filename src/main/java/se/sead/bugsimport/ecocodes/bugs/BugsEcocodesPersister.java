package se.sead.bugsimport.ecocodes.bugs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.Persister;
import se.sead.bugsimport.ecocodes.bugs.bugsmodel.EcoBugs;
import se.sead.bugsimport.ecocodes.seadmodel.Ecocode;
import se.sead.repositories.EcocodeRepository;

@Component
public class BugsEcocodesPersister extends Persister<EcoBugs, Ecocode> {

    @Autowired
    private EcocodeRepository repository;

    @Override
    protected Ecocode save(Ecocode seadValue) {
        return repository.saveOrUpdate(seadValue);
    }
}
