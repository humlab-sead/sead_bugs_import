package se.sead.bugsimport.datasetcontacts.updater;

import org.junit.Before;
import org.junit.Test;
import se.sead.sead.contact.Contact;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SiteContactParserTest {

    private SiteContactParser parser;

    @Before
    public void setup(){
        parser = new SiteContactParser();
    }

    @Test
    public void noContacts(){
        List<Contact> contacts = createContactDataAndParse(null, null);
        assertEquals(Collections.EMPTY_LIST, contacts);
    }

    private List<Contact> createContactDataAndParse(String identifiedBy, String specimenRepository) {
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
        List<Contact> contacts = createContactDataAndParse("Archer", null);
        assertEquals("Archer", contacts.get(0).getLastName());
    }

    @Test
    public void specimenRepository(){
        List<Contact> contacts = createContactDataAndParse(null, "University of Ottawa");
        assertEquals("University of Ottawa", contacts.get(0).getFirstName());
    }

    @Test
    public void oneIdentifiedByAndSpecimenRepository(){
        List<Contact> contacts = createContactDataAndParse("Archer", "University of Ottawa");
        assertEquals(2, contacts.size());
        assertEquals("Archer", contacts.get(0).getLastName());
        assertEquals("University of Ottawa", contacts.get(1).getFirstName());
    }

    @Test
    public void twoIdentifiedByCommaSeparated(){
        List<Contact> contacts = createContactDataAndParse("Archer, Bookland", null);
        assertEquals(2, contacts.size());
        assertEquals("Archer", contacts.get(0).getLastName());
        assertEquals("Bookland", contacts.get(1).getLastName());
    }
}
