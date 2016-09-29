package se.sead.bugsimport.lab;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.Persister;
import se.sead.bugsimport.lab.bugsmodel.BugsLab;
import se.sead.bugsimport.lab.seadmodel.DatingLab;
import se.sead.repositories.DatingLabRepository;

@Component
public class LabPersister extends Persister<BugsLab, DatingLab> {

    @Autowired
    private DatingLabRepository repository;

    @Override
    protected DatingLab save(DatingLab seadValue) {
        return repository.saveOrUpdate(seadValue);
    }
}
