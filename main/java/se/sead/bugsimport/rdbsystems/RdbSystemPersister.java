package se.sead.bugsimport.rdbsystems;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.Persister;
import se.sead.bugsimport.rdbsystems.bugsmodel.BugsRDBSystem;
import se.sead.bugsimport.rdbsystems.seadmodel.RdbSystem;
import se.sead.repositories.RDBSystemRepository;

@Component
public class RdbSystemPersister extends Persister<BugsRDBSystem, RdbSystem> {

    @Autowired
    private RDBSystemRepository rdbSystemRepository;

    @Override
    protected RdbSystem save(RdbSystem seadValue) {
        return rdbSystemRepository.saveOrUpdate(seadValue);
    }
}
