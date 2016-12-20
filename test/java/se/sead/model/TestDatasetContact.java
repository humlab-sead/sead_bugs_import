package se.sead.model;

import se.sead.sead.contact.Contact;
import se.sead.sead.contact.ContactType;
import se.sead.sead.data.Dataset;
import se.sead.sead.data.DatasetContact;

public class TestDatasetContact extends DatasetContact {

    private TestDatasetContact(Integer id){
        super.setId(id);
    }

    public static DatasetContact create(
            Integer id,
            Contact contact,
            ContactType type,
            Dataset dataset
    ){
        DatasetContact datasetContact = new TestDatasetContact(id);
        datasetContact.setDataset(dataset);
        datasetContact.setContact(contact);
        datasetContact.setType(type);
        return datasetContact;
    }
}
