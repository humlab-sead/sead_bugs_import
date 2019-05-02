package se.sead.bugsimport.sitereferences;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.Persister;
import se.sead.bugsimport.sitereferences.bugsmodel.BugsSiteRef;
import se.sead.bugsimport.sitereferences.seadmodel.SiteReference;
import se.sead.repositories.SiteReferenceRepository;

@Component
public class SiteReferencePersister extends Persister<BugsSiteRef, SiteReference> {

    @Autowired
    private SiteReferenceRepository repository;

    @Override
    protected SiteReference save(SiteReference seadValue) {
        return repository.saveOrUpdate(seadValue);
    }
}
