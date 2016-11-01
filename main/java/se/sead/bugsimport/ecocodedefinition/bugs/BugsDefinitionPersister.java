package se.sead.bugsimport.ecocodedefinition.bugs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.Persister;
import se.sead.bugsimport.ecocodedefinition.bugs.bugsmodel.EcoDefBugs;
import se.sead.bugsimport.ecocodedefinition.seadmodel.EcocodeDefinition;
import se.sead.repositories.EcocodeDefinitionRepository;

@Component
public class BugsDefinitionPersister extends Persister<EcoDefBugs, EcocodeDefinition> {

    @Autowired
    private EcocodeDefinitionRepository repository;

    @Override
    protected EcocodeDefinition save(EcocodeDefinition seadValue) {
        return repository.saveOrUpdate(seadValue);
    }
}
