package se.sead.bugsimport.ecocodedefinitiongroups;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.Persister;
import se.sead.bugsimport.ecocodedefinitiongroups.bugsmodel.EcoDefGroups;
import se.sead.bugsimport.ecocodedefinitiongroups.seadmodel.EcocodeGroup;
import se.sead.repositories.EcocodeGroupRepository;

@Component
public class EcocodeGroupPersister extends Persister<EcoDefGroups, EcocodeGroup> {

    @Autowired
    private EcocodeGroupRepository repository;

    @Override
    protected EcocodeGroup save(EcocodeGroup seadValue) {
        return repository.saveOrUpdate(seadValue);
    }
}
