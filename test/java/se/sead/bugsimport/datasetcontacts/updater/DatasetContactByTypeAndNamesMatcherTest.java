package se.sead.bugsimport.datasetcontacts.updater;

import org.junit.Before;
import org.junit.Test;
import se.sead.sead.contact.Contact;
import se.sead.sead.contact.ContactType;
import se.sead.sead.data.DatasetContact;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DatasetContactByTypeAndNamesMatcherTest {

    private DatasetContactByTypeAndNamesMatcher matcher = new DatasetContactByTypeAndNamesMatcher();

    private ContactType qType;
    private ContactType oType;

    @Before
    public void setup(){
        qType = new ContactType();
        qType.setName("Q");
        oType = new ContactType();
        oType.setName("O");
    }


    @Test
    public void listIsEmpty(){
        boolean result = matcher.existIn(createContact(qType, "first", "last"), Collections.EMPTY_LIST);
        assertFalse(result);
    }

    private DatasetContact createContact(ContactType type, String firstName, String lastName){
        Contact contact = new Contact();
        contact.setFirstName(firstName);
        contact.setLastName(lastName);
        DatasetContact datasetContact = new DatasetContact();
        datasetContact.setContact(contact);
        datasetContact.setType(type);
        return datasetContact;
    }

    @Test
    public void listContainItem(){
        boolean result = matcher.existIn(
                createContact(qType, "first", "last"),
                Arrays.asList(
                        createContact(qType, "first", "last")
                ));
        assertTrue(result);
    }

    @Test
    public void listDoNotContainItemFromOtherType(){
        boolean result = matcher.existIn(
                createContact(qType,"first", "last"),
                Arrays.asList(
                        createContact(oType, "first", "last")
                )
        );
        assertFalse(result);
    }

    @Test
    public void listDoNotContainNameMismatch(){
        boolean result = matcher.existIn(
                createContact(qType, "first", "last"),
                Arrays.asList(
                        createContact(qType, "first", "Wrong")
                )
        );
        assertFalse(result);
    }

    @Test
    public void listIsLargeButContainItem(){
        List<DatasetContact> testItems = new ArrayList<>();
        testItems.addAll(
                IntStream.range(0, 20)
                        .mapToObj(firstName -> createContact(qType, "first" + firstName, "last"))
                        .collect(Collectors.toList())
        );
        testItems.add(createContact(qType, "first", "last"));
        boolean result = matcher.existIn(
                createContact(qType, "first", "last"),
                testItems
        );
        assertTrue(result);
    }

    @Test
    public void listIsLargeAndDoNotContainItem(){
        List<DatasetContact> testItems = new ArrayList<>();
        testItems.addAll(
                IntStream.range(0,20)
                .mapToObj(name -> createContact(qType, "first" + name, "last"))
                .collect(Collectors.toList())
        );
        boolean result = matcher.existIn(
                createContact(qType, "first", "last"),
                testItems
        );
        assertFalse(result);
    }
}
