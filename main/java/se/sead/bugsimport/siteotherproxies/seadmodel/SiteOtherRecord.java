package se.sead.bugsimport.siteotherproxies.seadmodel;

import se.sead.bugsimport.site.seadmodel.SeadSite;
import se.sead.bugsimport.bibliography.seadmodel.Biblio;
import se.sead.sead.model.LoggableEntity;
import se.sead.sead.recordtypes.RecordType;

import javax.persistence.*;

@Entity
@Table(name = "tbl_site_other_records")
@SequenceGenerator(name="other_record_id_seq", sequenceName = "tbl_site_other_records_site_other_records_id_seq")
public class SiteOtherRecord extends LoggableEntity {

    @Id
    @GeneratedValue(generator = "other_record_id_seq", strategy = GenerationType.IDENTITY)
    @Column(name = "site_other_records_id", nullable = false)
    private Integer id;
    @Column(name="description")
    private String description;
    @ManyToOne
    @JoinColumn(name = "site_id")
    private SeadSite site;
    @ManyToOne
    @JoinColumn(name = "biblio_id")
    private Biblio reference;
    @ManyToOne
    @JoinColumn(name = "record_type_id")
    private RecordType recordType;

    @Override
    public Integer getId() {
        return id;
    }

    protected void setId(Integer id){
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SeadSite getSite() {
        return site;
    }

    public void setSite(SeadSite site) {
        this.site = site;
    }

    public Biblio getReference() {
        return reference;
    }

    public void setReference(Biblio reference) {
        this.reference = reference;
    }

    public RecordType getRecordType() {
        return recordType;
    }

    public void setRecordType(RecordType recordType) {
        this.recordType = recordType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof SiteOtherRecord)) return false;

        SiteOtherRecord that = (SiteOtherRecord) o;

        if (id != null && that.id != null && !id.equals(that.id)) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (site != null ? !site.equals(that.site) : that.site != null) return false;
        if (reference != null ? !reference.equals(that.reference) : that.reference != null) return false;
        return recordType != null ? recordType.equals(that.recordType) : that.recordType == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (site != null ? site.hashCode() : 0);
        result = 31 * result + (reference != null ? reference.hashCode() : 0);
        result = 31 * result + (recordType != null ? recordType.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SiteOtherRecord{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", site=" + site +
                ", reference=" + reference +
                ", recordType=" + recordType +
                '}';
    }
}
