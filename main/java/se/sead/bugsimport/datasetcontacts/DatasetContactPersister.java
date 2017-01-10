package se.sead.bugsimport.datasetcontacts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.Persister;
import se.sead.bugsimport.countsheets.bugsmodel.Countsheet;
import se.sead.bugsimport.datasetcontacts.updater.ContactCacheAndRepositoryAccessor;
import se.sead.repositories.DatasetContactRepository;
import se.sead.sead.data.DatasetContact;

@Component
public class DatasetContactPersister extends Persister<Countsheet, DatasetContact> {

    @Autowired
    private DatasetContactRepository repository;
    @Autowired
    private ContactCacheAndRepositoryAccessor contactCacheAndRepositoryAccessor;

    @Override
    protected DatasetContact save(DatasetContact seadValue) {
        syncContact(seadValue);
        DatasetContact datasetContact = repository.saveOrUpdate(seadValue);
        syncSavedContact(datasetContact);
        return datasetContact;
    }

    private void syncSavedContact(DatasetContact datasetContact) {
        contactCacheAndRepositoryAccessor.setContact(datasetContact.getContact());
    }

    private void syncContact(DatasetContact seadValue) {
        if(seadValue.getContact().isNewItem()){
            seadValue.setContact(contactCacheAndRepositoryAccessor.getFromCacheOrRepository(seadValue.getContact()));
        }
    }
}
