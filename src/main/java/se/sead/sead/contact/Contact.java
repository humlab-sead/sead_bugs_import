package se.sead.sead.contact;

import se.sead.sead.model.LoggableEntity;

import javax.persistence.*;

@Entity
@Table(name = "tbl_contacts")
@SequenceGenerator(name = "contact_id_gen", sequenceName = "tbl_contacts_contact_id_seq")
public class Contact extends LoggableEntity {

    @Id
    @GeneratedValue(generator = "contact_id_gen", strategy = GenerationType.IDENTITY)
    @Column(name = "contact_id", nullable = false)
    private Integer id;
    @Column(name = "address_1")
    private String address1;
    @Column(name = "address_2")
    private String address2;
    @Column(name = "email")
    private String email;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "url")
    private String url;

    @Override
    public Integer getId() {
        return id;
    }

    protected void setId(Integer id) {
        this.id = id;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Contact)) return false;

        Contact contact = (Contact) o;

        if (id != null ? !id.equals(contact.id) : contact.id != null) return false;
        if (address1 != null ? !address1.equals(contact.address1) : contact.address1 != null) return false;
        if (address2 != null ? !address2.equals(contact.address2) : contact.address2 != null) return false;
        if (email != null ? !email.equals(contact.email) : contact.email != null) return false;
        if (firstName != null ? !firstName.equals(contact.firstName) : contact.firstName != null) return false;
        if (lastName != null ? !lastName.equals(contact.lastName) : contact.lastName != null) return false;
        if (phoneNumber != null ? !phoneNumber.equals(contact.phoneNumber) : contact.phoneNumber != null) return false;
        return url != null ? url.equals(contact.url) : contact.url == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (address1 != null ? address1.hashCode() : 0);
        result = 31 * result + (address2 != null ? address2.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", address1='" + address1 + '\'' +
                ", address2='" + address2 + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
