package se.sead.bugsimport.speciesassociation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.Persister;
import se.sead.bugsimport.speciesassociation.bugsmodel.BugsSpeciesAssociation;
import se.sead.bugsimport.speciesassociation.seadmodel.SpeciesAssociation;
import se.sead.repositories.SpeciesAssociationRepository;

@Component
public class SpeciesAssociationPersister extends Persister<BugsSpeciesAssociation, SpeciesAssociation> {

    @Autowired
    private SpeciesAssociationRepository repository;

    @Override
    protected SpeciesAssociation save(SpeciesAssociation seadValue) {
        return repository.saveOrUpdate(seadValue);
    }
}
