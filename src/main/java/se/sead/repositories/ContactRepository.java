package se.sead.repositories;

import org.springframework.data.repository.Repository;
import se.sead.sead.contact.Contact;

import java.util.List;

public interface ContactRepository extends Repository<Contact, Integer> {
    List<Contact> findAll();

    Contact getByFirstNameAndLastName(String firstName, String lastName);
}
