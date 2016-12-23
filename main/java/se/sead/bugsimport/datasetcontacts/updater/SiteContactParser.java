package se.sead.bugsimport.datasetcontacts.updater;

import se.sead.sead.contact.Contact;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SiteContactParser {

    private MultipleIdentifiedByContactsParser multipleIdentifiedByContactsParser = new MultipleIdentifiedByContactsParser();

    public List<Contact> parseIntoNonDatabaseSyncedContacts(SiteContactReader.SiteContactStringData contactData){
        List<Contact> parsedContacts = new ArrayList<>();
        parsedContacts.addAll(parseIdentifiedBy(contactData.getIdentifiedBy()));
        parsedContacts.addAll(parseSpecimenRepository(contactData.getSpecimenRepository()));
        return parsedContacts;
    }

    private List<Contact> parseIdentifiedBy(String identifiedBy){
        if(identifiedBy == null ||identifiedBy.isEmpty()){
            return Collections.EMPTY_LIST;
        }
        if(multipleIdentifiedByContactsParser.isMultipleContacts(identifiedBy)){
            return createContactsFromSplitIdentifiedBy(multipleIdentifiedByContactsParser.splitContacts(identifiedBy));
        }
        return Arrays.asList(
                createContact(null, identifiedBy)
        );
    }

    private List<Contact> createContactsFromSplitIdentifiedBy(List<String> splitContactNames){
        return splitContactNames
                .stream()
                .map(name -> createContact(null, name.trim()))
                .collect(Collectors.toList());
    }

    private Contact createContact(String firstName, String lastName){
        Contact contact = new Contact();
        contact.setLastName(lastName);
        contact.setFirstName(firstName);
        return contact;
    }

    private List<Contact> parseSpecimenRepository(String specimenRepository){
        if(specimenRepository == null ||specimenRepository.isEmpty()){
            return Collections.EMPTY_LIST;
        }
        return Arrays.asList(
                createContact(specimenRepository, null)
        );
    }

}
