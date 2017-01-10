package se.sead.bugsimport.datasetcontacts.updater;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.repositories.ContactTypeRepository;
import se.sead.sead.data.Dataset;
import se.sead.sead.data.DatasetContact;

import java.util.List;

@Component
public class DatasetContactUpdater {

    private final DatasetContactByTypeAndNamesMatcher datasetContactByTypeAndNamesMatcher = new DatasetContactByTypeAndNamesMatcher();
    @Autowired
    private SiteContactReader siteContactReader;
    private SiteContactToDatasetContactParser parser;

    @Autowired
    public DatasetContactUpdater(ContactTypeRepository contactTypeRepository, ContactCacheAndRepositoryAccessor contactCacheAndRepositoryAccessor) {
        this.parser = new SiteContactToDatasetContactParser(contactTypeRepository, contactCacheAndRepositoryAccessor);
    }

    public void update(Dataset original, String siteCode){
        List<DatasetContact> contactsFromTSite = getContactsFromTSite(siteCode);
        update(original, contactsFromTSite);
    }

    private List<DatasetContact> getContactsFromTSite(String siteCode){
        SiteContactReader.SiteContactStringData siteContacts = siteContactReader.parse(siteCode);
        return parser.parseIntoNonDatabaseSyncedContacts(siteContacts);
    }

    private void update(Dataset dataset, List<DatasetContact> contactsFromTSite){
        if(dataset.getContacts() == null || dataset.getContacts().isEmpty()){
            dataset.setContacts(contactsFromTSite);
        } else if(!contactsFromTSite.isEmpty()){
            contactsFromTSite.removeIf(
                    datasetContact -> existIn(datasetContact, dataset.getContacts())
            );
            dataset.getContacts().addAll(contactsFromTSite);
        }
        dataset.getContacts().forEach(datasetContact -> datasetContact.setDataset(dataset));
    }

    private boolean existIn(DatasetContact queriedDatasetContact, List<DatasetContact> collection){
        return datasetContactByTypeAndNamesMatcher.existIn(queriedDatasetContact, collection);
    }

}
