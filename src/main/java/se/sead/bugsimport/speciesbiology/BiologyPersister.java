package se.sead.bugsimport.speciesbiology;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.Persister;
import se.sead.bugsimport.speciesbiology.bugsmodel.Biology;
import se.sead.bugsimport.speciesbiology.seadmodel.TextBiology;
import se.sead.repositories.TextBiologyDataRepository;

/**
 * Created by erer0001 on 2016-05-18.
 */
@Component
public class BiologyPersister extends Persister<Biology, TextBiology> {

    @Autowired
    private TextBiologyDataRepository biologyDataRepository;

    @Override
    protected TextBiology save(TextBiology seadValue) {
        return biologyDataRepository.saveOrUpdate(seadValue);
    }
}
