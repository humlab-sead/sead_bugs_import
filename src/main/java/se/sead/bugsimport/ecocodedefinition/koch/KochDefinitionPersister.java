package se.sead.bugsimport.ecocodedefinition.koch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.Persister;
import se.sead.bugsimport.ecocodedefinition.koch.bugsmodel.EcoDefKoch;
import se.sead.bugsimport.ecocodedefinition.seadmodel.EcocodeDefinition;
import se.sead.repositories.EcocodeDefinitionRepository;

@Component
public class KochDefinitionPersister extends Persister<EcoDefKoch, EcocodeDefinition>{

    @Autowired
    private EcocodeDefinitionRepository repository;

    @Override
    protected EcocodeDefinition save(EcocodeDefinition seadValue) {
        return repository.saveOrUpdate(seadValue);
    }
}
