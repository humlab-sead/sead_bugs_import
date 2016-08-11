package se.sead.bugsimport.sitereferences.seadmodel;

import se.sead.bugsimport.site.seadmodel.SeadSite;
import se.sead.sead.model.Biblio;
import se.sead.sead.model.LoggableEntity;

import javax.persistence.*;

@Entity
@Table(name = "tbl_site_references")
@SequenceGenerator(name="site_references_id_seq", sequenceName = "tbl_site_references_site_reference_id_seq")
public class SiteReference extends LoggableEntity {

    @Id
    @GeneratedValue(generator = "site_references_id_seq", strategy = GenerationType.AUTO)
    @Column(name="site_reference_id", nullable = false)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "site_id")
    private SeadSite site;
    @ManyToOne
    @JoinColumn(name="biblio_id")
    private Biblio reference;

    @Override
    public Integer getId() {
        return id;
    }

    protected void setId(Integer id){
        this.id = id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof SiteReference)) return false;

        SiteReference that = (SiteReference) o;

        if (id != null && that.id != null && !id.equals(that.id)) return false;
        if (!site.equals(that.site)) return false;
        return reference.equals(that.reference);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + site.hashCode();
        result = 31 * result + reference.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "SiteReference{" +
                "id=" + id +
                ", site=" + site +
                ", reference=" + reference +
                '}';
    }
}
