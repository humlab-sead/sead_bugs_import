package se.sead.bugsimport.datasetcontacts.updater;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.repositories.ContactRepository;
import se.sead.sead.contact.Contact;

@Component
public class ContactCacheAndRepositoryAccessor {

    private ContactCache cache;
    private ContactRepository repository;

    @Autowired
    public ContactCacheAndRepositoryAccessor(ContactRepository repository) {
        this.repository = repository;
        cache = new ContactCache();
    }

    public Contact getFromCacheOrRepository(Contact contact){
        Contact preStoredContact = cache.getCached(contact);
        if(preStoredContact != null){
            return preStoredContact;
        } else {
            preStoredContact = getFromRepository(contact);
            if(preStoredContact != null){
                setContact(preStoredContact);
                return preStoredContact;
            } else {
                setContact(contact);
                return contact;
            }
        }
    }

    public void setContact(Contact contact){
        cache.store(contact);
    }

    protected Contact getFromRepository(Contact contact) {
        return repository.getByFirstNameAndLastName(contact.getFirstName(), contact.getLastName());
    }

    public void clearCache(){
        cache.clear();
    }
}
