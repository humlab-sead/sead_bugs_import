package se.sead.bugsimport.mcrnames;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.Persister;
import se.sead.bugsimport.mcrnames.bugsmodel.BugsMCRNames;
import se.sead.bugsimport.mcrnames.seadmodel.MCRName;
import se.sead.repositories.McrNamesRepository;

@Component
public class McrNamesPersister extends Persister<BugsMCRNames, MCRName> {

    @Autowired
    private McrNamesRepository repository;

    @Override
    protected MCRName save(MCRName seadValue) {
        return repository.saveOrUpdate(seadValue);
    }
}
