package se.sead.bugsimport.datasetcontacts.updater;

import se.sead.sead.contact.Contact;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class SpecimenParser {

    private ContactCreator contactCreator = new ContactCreator();

    List<Contact> parseSpecimenRepository(String specimenRepository) {
        if (specimenRepository == null || specimenRepository.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        return Arrays.asList(
                contactCreator.createContact(specimenRepository, null)
        );
    }
}
