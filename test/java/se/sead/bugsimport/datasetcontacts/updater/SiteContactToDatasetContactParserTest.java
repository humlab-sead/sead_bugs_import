package se.sead.bugsimport.datasetcontacts.updater;

import org.junit.Before;
import org.junit.Test;
import se.sead.repositories.ContactRepository;
import se.sead.repositories.ContactTypeRepository;
import se.sead.sead.contact.Contact;
import se.sead.sead.contact.ContactType;
import se.sead.sead.data.DatasetContact;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SiteContactToDatasetContactParserTest {

    private SiteContactToDatasetContactParser parser;
    private MockContactTypeRepository typeRepository = new MockContactTypeRepository();
    private ContactCacheAndRepositoryAccessor accessor = new MockContactCacheAndRepositoryAccessor();

    @Before
    public void setup(){
        parser = new SiteContactToDatasetContactParser(typeRepository, accessor);
    }

    @Test
    public void noContacts(){
        List<DatasetContact> contacts = createContactDataAndParse(null, null);
        assertEquals(Collections.EMPTY_LIST, contacts);
    }

    private List<DatasetContact> createContactDataAndParse(String identifiedBy, String specimenRepository) {
        SiteContactReader.SiteContactStringData contactData = createContactData(identifiedBy, specimenRepository);
        return parser.parseIntoNonDatabaseSyncedContacts(contactData);
    }

    private SiteContactReader.SiteContactStringData createContactData(String identifiedBy, String specimenRepository) {
        SiteContactReader.SiteContactStringData contactData = new SiteContactReader.SiteContactStringData();
        contactData.setIdentifiedBy(identifiedBy);
        contactData.setSpecimenRepository(specimenRepository);
        return contactData;
    }

    @Test
    public void oneIdentifiedBy(){
        List<DatasetContact> contacts = createContactDataAndParse("Archer", null);
        assertEquals("Archer", contacts.get(0).getContact().getLastName());
        assertEquals(typeRepository.identifiedBy, contacts.get(0).getType());
    }

    @Test
    public void specimenRepository(){
        List<DatasetContact> contacts = createContactDataAndParse(null, "University of Ottawa");
        assertEquals("University of Ottawa", contacts.get(0).getContact().getFirstName());
        assertEquals(typeRepository.specimen, contacts.get(0).getType());
    }

    @Test
    public void oneIdentifiedByAndSpecimenRepository(){
        List<DatasetContact> contacts = createContactDataAndParse("Archer", "University of Ottawa");
        assertEquals(2, contacts.size());
        assertEquals("Archer", contacts.get(0).getContact().getLastName());
        assertEquals(typeRepository.identifiedBy, contacts.get(0).getType());
        assertEquals("University of Ottawa", contacts.get(1).getContact().getFirstName());
        assertEquals(typeRepository.specimen, contacts.get(1).getType());
    }

    @Test
    public void twoIdentifiedByCommaSeparated(){
        List<DatasetContact> contacts = createContactDataAndParse("Archer, Bookland", null);
        assertEquals(2, contacts.size());
        assertEquals("Archer", contacts.get(0).getContact().getLastName());
        assertEquals(typeRepository.identifiedBy, contacts.get(0).getType());
        assertEquals("Bookland", contacts.get(1).getContact().getLastName());
        assertEquals(typeRepository.identifiedBy, contacts.get(1).getType());
    }

    static class MockContactTypeRepository implements ContactTypeRepository {

        ContactType identifiedBy = new ContactType();
        ContactType specimen = new ContactType();

        @Override
        public ContactType getIdentifiedByType() {
            return identifiedBy;
        }

        @Override
        public ContactType findOne(Integer integer) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ContactType saveOrUpdate(ContactType entity) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ContactType getSpecimentRepositoryType() {
            return specimen;
        }
    }

    static class MockContactCacheAndRepositoryAccessor extends ContactCacheAndRepositoryAccessor {

        public MockContactCacheAndRepositoryAccessor() {
            super(null);
        }

        @Override
        protected Contact getFromRepository(Contact contact) {
            return null;
        }
    }
}
