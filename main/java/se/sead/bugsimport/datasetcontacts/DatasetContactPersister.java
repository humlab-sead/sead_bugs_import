package se.sead.bugsimport.datasetcontacts;

import org.springframework.stereotype.Component;
import se.sead.bugsimport.Persister;
import se.sead.bugsimport.countsheets.bugsmodel.Countsheet;
import se.sead.sead.data.DatasetContact;

@Component
public class DatasetContactPersister extends Persister<Countsheet, DatasetContact> {

    @Override
    protected DatasetContact save(DatasetContact seadValue) {
        return null;
    }
}
