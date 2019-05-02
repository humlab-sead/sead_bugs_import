package se.sead.sead.data;

import se.sead.sead.contact.Contact;
import se.sead.sead.contact.ContactType;
import se.sead.sead.model.LoggableEntity;

import javax.persistence.*;

@Entity
@Table(name = "tbl_dataset_contacts")
@SequenceGenerator(name = "dataset_contact_id_gen", sequenceName = "tbl_dataset_contacts_dataset_contact_id_seq")
public class DatasetContact extends LoggableEntity {

    @Id
    @GeneratedValue(generator = "dataset_contact_id_gen", strategy = GenerationType.IDENTITY)
    @Column(name = "dataset_contact_id", nullable = false)
    private Integer id;
    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "contact_id", nullable = false)
    private Contact contact;
    @ManyToOne(cascade =  {CascadeType.MERGE})
    @JoinColumn(name = "contact_type_id", nullable = false)
    private ContactType type;
    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "dataset_id", nullable = false)
    private Dataset dataset;

    @Override
    public Integer getId() {
        return id;
    }

    protected void setId(Integer id) {
        this.id = id;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public ContactType getType() {
        return type;
    }

    public void setType(ContactType type) {
        this.type = type;
    }

    public Dataset getDataset() {
        return dataset;
    }

    public void setDataset(Dataset dataset) {
        this.dataset = dataset;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof DatasetContact)) return false;

        DatasetContact that = (DatasetContact) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (contact != null ? !contact.equals(that.contact) : that.contact != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        return dataset != null ? dataset.equals(that.dataset) : that.dataset == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (contact != null ? contact.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (dataset != null ? dataset.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DatasetContact{" +
                "id=" + id +
                ", contact=" + contact +
                ", type=" + type +
                ", dataset=" + dataset +
                '}';
    }
}
