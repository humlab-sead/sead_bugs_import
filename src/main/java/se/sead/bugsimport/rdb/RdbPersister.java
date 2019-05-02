package se.sead.bugsimport.rdb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.Persister;
import se.sead.bugsimport.rdb.bugsmodel.BugsRDB;
import se.sead.bugsimport.rdb.seadmodel.Rdb;
import se.sead.repositories.RdbRepository;

@Component
public class RdbPersister extends Persister<BugsRDB, Rdb> {

    @Autowired
    private RdbRepository rdbRepository;

    @Override
    protected Rdb save(Rdb seadValue) {
        return rdbRepository.saveOrUpdate(seadValue);
    }
}
