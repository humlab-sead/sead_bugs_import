package se.sead.datasetcontacts;

import se.sead.model.TestContact;
import se.sead.model.TestEqualityHelper;
import se.sead.repositories.ContactRepository;
import se.sead.sead.contact.Contact;
import se.sead.testutils.DatabaseContentVerification;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

class ContactDatabaseContentProvider implements DatabaseContentVerification.DatabaseContentTestDataProvider<Contact>{

    private ContactRepository contactRepository;

    ContactDatabaseContentProvider(ContactRepository contactRepository){
        this.contactRepository = contactRepository;
    }

    @Override
    public List<Contact> getExpectedData() {
        return Arrays.asList(
                TestContact.create(1, null, null, null, null, "University of Ottawa", null, null),
                TestContact.create(2, null, null, null, null, "University of Bolivia", null, null),
                TestContact.create(3, null, null, null, null, null, "First", null),
                TestContact.create(4, null, null, null, null, null, "Second", null),
                TestContact.create(null, null, null, null, null, null, "Archer", null),
                TestContact.create(null, null, null, null, null, "University of Carpatia", null, null),
                TestContact.create(null, null, null, null, null, null, "A new person", null),
                TestContact.create(null, null, null, null, null, null, "Arnold (only half)", null)
        );
    }

    @Override
    public List<Contact> getActualData() {
        return contactRepository.findAll();
    }

    @Override
    public Comparator<Contact> getSorter() {
        return new ContactComparator();
    }

    @Override
    public TestEqualityHelper<Contact> getEqualityHelper() {
        return new TestEqualityHelper<>();
    }

    static class ContactComparator implements Comparator<Contact>{
        @Override
        public int compare(Contact o1, Contact o2) {
            String o1String = getName(o1);
            String o2String = getName(o2);
            return o1String.compareTo(o2String);
        }

        private String getName(Contact contact){
            return contact.getFirstName() != null ?
                    contact.getFirstName() :
                    contact.getLastName() != null ?
                            contact.getLastName() : "";
        }
    }
}
