package se.sead.bugsimport.rdbcodes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.Persister;
import se.sead.bugsimport.rdbcodes.bugsmodel.BugsRDBCodes;
import se.sead.bugsimport.rdbcodes.seadmodel.RdbCode;
import se.sead.repositories.RdbCodeRepository;

@Component
public class RdbCodePersister extends Persister<BugsRDBCodes, RdbCode> {

    @Autowired
    private RdbCodeRepository rdbCodeRepository;

    @Override
    protected RdbCode save(RdbCode seadValue) {
        return rdbCodeRepository.saveOrUpdate(seadValue);
    }
}
