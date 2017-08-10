package se.sead.bugsimport.datasetcontacts.updater;

import se.sead.sead.contact.Contact;

public class ContactCreator {

    public Contact createContact(String firstName, String lastName){
        Contact contact = new Contact();
        contact.setLastName(lastName);
        contact.setFirstName(firstName);
        if(hasParenthesis(firstName) || hasParenthesis(lastName)){
            contact.setFlagged(true);
        }
        return contact;
    }

    private boolean hasParenthesis(String name){
        return name != null &&
                (name.contains("(") || name.contains(")"));
    }
}
