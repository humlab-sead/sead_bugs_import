package se.sead.bugsimport.datasetcontacts.updater;

import se.sead.sead.contact.Contact;

import java.util.HashMap;
import java.util.Map;

public class ContactCache {

    private Map<String, Contact> cache;

    public ContactCache(){
        cache = new HashMap<>();
    }

    public Contact getCached(Contact contact){
        String key = createKey(contact);
        return cache.get(key);
    }

    public void store(Contact contact){
        String key = createKey(contact);
        cache.put(key, contact);
    }

    private String createKey(Contact contact){
        return contact.getFirstName() != null ? contact.getFirstName() : " " +
                contact.getLastName() != null ? contact.getLastName() : " ";
    }

    public void clear() {
        cache.clear();
    }
}
