package se.sead.model;

import se.sead.sead.contact.Contact;

public class TestContact extends Contact {

    private TestContact(Integer id){
        super.setId(id);
    }

    public static Contact create(
            Integer id,
            String address1,
            String address2,
            String email,
            String phone,
            String firstName,
            String lastName,
            String url
    ){
        Contact contact = new TestContact(id);
        contact.setAddress1(address1);
        contact.setAddress2(address2);
        contact.setEmail(email);
        contact.setPhoneNumber(phone);
        contact.setFirstName(firstName);
        contact.setLastName(lastName);
        contact.setUrl(url);
        return contact;
    }
}
