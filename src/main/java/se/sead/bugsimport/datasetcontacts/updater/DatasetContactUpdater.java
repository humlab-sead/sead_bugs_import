package se.sead.bugsimport.datasetcontacts.updater;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.repositories.ContactTypeRepository;
import se.sead.repositories.DatasetContactRepository;
import se.sead.sead.data.Dataset;
import se.sead.sead.data.DatasetContact;

import java.util.List;

@Component
public class DatasetContactUpdater {

    private final DatasetContactByTypeAndNamesMatcher datasetContactByTypeAndNamesMatcher = new DatasetContactByTypeAndNamesMatcher();
    @Autowired
    private SiteContactReader siteContactReader;
    private SiteContactToDatasetContactParser parser;
    private DatasetContactRepository datasetContactRepository;

    @Autowired
    public DatasetContactUpdater(
            ContactTypeRepository contactTypeRepository,
            ContactCacheAndRepositoryAccessor contactCacheAndRepositoryAccessor,
            DatasetContactRepository datasetContactRepository
    ) {
        this.datasetContactRepository = datasetContactRepository;
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
        List<DatasetContact> originals = datasetContactRepository.findByDataset(dataset);
        if(originals == null || originals.isEmpty()){
            originals = contactsFromTSite;
        } else if(!contactsFromTSite.isEmpty()){
            updateContactsFromTSite(contactsFromTSite, originals);
            originals.addAll(contactsFromTSite);
        }
        originals.forEach(datasetContact -> datasetContact.setDataset(dataset));
        dataset.setContacts(originals);
    }

    private void updateContactsFromTSite(List<DatasetContact> contactsFromTSite, List<DatasetContact> seadStoredDatasetContacts) {
        contactsFromTSite.removeIf(
                datasetContact -> existIn(datasetContact, seadStoredDatasetContacts)
        );
    }

    private boolean existIn(DatasetContact queriedDatasetContact, List<DatasetContact> collection){
        return datasetContactByTypeAndNamesMatcher.existIn(queriedDatasetContact, collection);
    }

}
