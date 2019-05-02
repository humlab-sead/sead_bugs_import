package se.sead.bugsimport.datasetcontacts.updater;

import se.sead.sead.contact.Contact;
import se.sead.sead.contact.ContactType;
import se.sead.sead.data.DatasetContact;

import java.util.List;

public class DatasetContactByTypeAndNamesMatcher {
    public DatasetContactByTypeAndNamesMatcher() {
    }

    boolean existIn(DatasetContact queriedDatasetContact, List<DatasetContact> collection) {
        Contact queriedContact = queriedDatasetContact.getContact();
        ContactType queriedContactType = queriedDatasetContact.getType();
        return collection.stream()
                .anyMatch(
                        collectionItem ->
                                matchOnTypeAndFirstNameAndLastName(collectionItem, queriedContactType, queriedContact)
                );
    }

    static boolean matchOnTypeAndFirstNameAndLastName(DatasetContact underQuestion, ContactType type, Contact nameData) {
        String firstNameUnderQuestion = underQuestion.getContact() != null ? underQuestion.getContact().getFirstName() : null;
        String lastNameUnderQuestion = underQuestion.getContact() != null ? underQuestion.getContact().getLastName() : null;
        ContactType typeUnderQuestion = underQuestion.getType();
        if (typeUnderQuestion == null) {
            throw new NullPointerException();
        }
        return
                typeUnderQuestion.equals(type) &&
                        (firstNameUnderQuestion != null ? firstNameUnderQuestion.equals(nameData.getFirstName()) : nameData.getFirstName() == null) &&
                        (lastNameUnderQuestion != null ? lastNameUnderQuestion.equals(nameData.getLastName()) : nameData.getLastName() == null);
    }
}