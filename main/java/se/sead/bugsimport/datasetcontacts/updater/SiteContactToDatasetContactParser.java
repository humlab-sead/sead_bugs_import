package se.sead.bugsimport.datasetcontacts.updater;

import se.sead.repositories.ContactTypeRepository;
import se.sead.sead.contact.Contact;
import se.sead.sead.contact.ContactType;
import se.sead.sead.data.DatasetContact;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class SiteContactToDatasetContactParser {

    private IdentifiedByParser identifiedByParser = new IdentifiedByParser();
    private SpecimenParser specimenParser = new SpecimenParser();
    private ContactTypeRepository typeRepository;
    private ContactCacheAndRepositoryAccessor contactCacheAndRepositoryAccessor;

    public SiteContactToDatasetContactParser(ContactTypeRepository contactTypeRepository, ContactCacheAndRepositoryAccessor cacheAndRepositoryAccessor){
        this.typeRepository = contactTypeRepository;
        this.contactCacheAndRepositoryAccessor = cacheAndRepositoryAccessor;
    }

    public List<DatasetContact> parseIntoNonDatabaseSyncedContacts(SiteContactReader.SiteContactStringData contactData){
        List<DatasetContact> parsedContacts = new ArrayList<>();
        parsedContacts.addAll(getIdentifiedBy(contactData));
        parsedContacts.addAll(getSpecimenData(contactData));
        return parsedContacts;
    }

    private Collection<? extends DatasetContact> getIdentifiedBy(SiteContactReader.SiteContactStringData contactData) {
        List<Contact> identifiedBy = identifiedByParser.parseIdentifiedBy(contactData.getIdentifiedBy());
        return identifiedBy.stream()
                .map(contact -> createDatasetContact(contact, typeRepository.getIdentifiedByType()))
                .collect(Collectors.toList());
    }

    private DatasetContact createDatasetContact(Contact contact, ContactType contactType){
        DatasetContact datasetContact = new DatasetContact();
        datasetContact.setContact(contactCacheAndRepositoryAccessor.getFromCacheOrRepository(contact));
        datasetContact.setType(contactType);
        return datasetContact;
    }

    private Collection<? extends DatasetContact> getSpecimenData(SiteContactReader.SiteContactStringData contactData) {
        List<Contact> contacts = specimenParser.parseSpecimenRepository(contactData.getSpecimenRepository());
        return contacts.stream()
                .map(contact -> createDatasetContact(contact, typeRepository.getSpecimentRepositoryType()))
                .collect(Collectors.toList());
    }
}
