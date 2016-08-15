package se.sead.bugsimport.countsheets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.Persister;
import se.sead.bugsimport.countsheets.bugsmodel.Countsheet;
import se.sead.bugsimport.countsheets.seadmodel.SampleGroup;
import se.sead.repositories.SampleGroupRepository;

@Component
public class SampleGroupPersister extends Persister<Countsheet, SampleGroup> {

    @Autowired
    private SampleGroupRepository sampleGroupRepository;

    @Override
    protected SampleGroup save(SampleGroup seadValue) {
        return sampleGroupRepository.saveOrUpdate(seadValue);
    }
}
