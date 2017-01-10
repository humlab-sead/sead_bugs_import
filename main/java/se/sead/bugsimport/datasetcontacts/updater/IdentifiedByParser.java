package se.sead.bugsimport.datasetcontacts.updater;

import se.sead.sead.contact.Contact;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class IdentifiedByParser {

    private MultipleIdentifiedByContactsParser multipleIdentifiedByContactsParser = new MultipleIdentifiedByContactsParser();
    private ContactCreator contactCreator = new ContactCreator();

    List<Contact> parseIdentifiedBy(String identifiedBy){
        if(identifiedBy == null ||identifiedBy.isEmpty()){
            return Collections.EMPTY_LIST;
        }
        if(multipleIdentifiedByContactsParser.isMultipleContacts(identifiedBy)){
            return createContactsFromSplitIdentifiedBy(multipleIdentifiedByContactsParser.splitContacts(identifiedBy));
        }
        return Arrays.asList(
                contactCreator.createContact(null, identifiedBy)
        );
    }

    private List<Contact> createContactsFromSplitIdentifiedBy(List<String> splitContactNames){
        return splitContactNames
                .stream()
                .map(name -> contactCreator.createContact(null, name.trim()))
                .collect(Collectors.toList());
    }
}
