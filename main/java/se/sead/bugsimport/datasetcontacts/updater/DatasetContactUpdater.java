package se.sead.bugsimport.datasetcontacts.updater;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.sead.contact.Contact;
import se.sead.sead.data.Dataset;
import se.sead.sead.data.DatasetContact;

import java.util.List;

@Component
public class DatasetContactUpdater {

    @Autowired
    private SiteContactReader siteContactReader;
    private SiteContactParser parser = new SiteContactParser();

    public void update(Dataset original, String siteCode){
        List<Contact> contactsFromTSite = getContactsFromTSite(siteCode);
        update(original, contactsFromTSite);
        throw new UnsupportedOperationException();
    }

    private List<Contact> getContactsFromTSite(String siteCode){
        SiteContactReader.SiteContactStringData siteContacts = siteContactReader.parse(siteCode);
        return parser.parseIntoNonDatabaseSyncedContacts(siteContacts);
    }

    private void update(Dataset dataset, List<Contact> contactsFromTSite){

    }

}
