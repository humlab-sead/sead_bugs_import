package se.sead.bugsimport.datasetcontacts.updater;

import se.sead.sead.contact.Contact;

public class ContactCreator {

    public Contact createContact(String firstName, String lastName){
        Contact contact = new Contact();
        contact.setLastName(lastName);
        contact.setFirstName(firstName);
        return contact;
    }
}
