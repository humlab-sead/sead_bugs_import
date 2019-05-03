package se.sead.bugsimport.bibliography.seadmodel;

import se.sead.sead.model.LoggableEntity;

import javax.persistence.*;

/**
 * Created by erer0001 on 2016-05-18.
 */
@Entity
@Table(name="tbl_biblio")
@SequenceGenerator(name = "biblio_seq_gen", sequenceName = "tbl_biblio_biblio_id_seq")
public class Biblio extends LoggableEntity {

    @Id
    @GeneratedValue(generator = "biblio_seq_gen")
    @Column(name="biblio_id", nullable = false)
    private Integer id;
    @Column(name="bugs_reference")
    private String bugsReference;
    @Column(name="authors")
    private String authors;
    @Column(name="full_reference")
    private String fullReference;
    @Column(name="title")
    private String title;

    @Override
    public Integer getId() {
        return id;
    }

    protected void setId(Integer id){
        this.id = id;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getBugsReference() {
        return bugsReference;
    }

    public void setBugsReference(String bugsReference) {
        this.bugsReference = bugsReference;
    }

    public String getFullReference() {
        return fullReference;
    }

    public void setFullReference(String fullReference) {
        this.fullReference = fullReference;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Biblio)) return false;

        Biblio biblio = (Biblio) o;

        if (id != null ? !id.equals(biblio.id) : biblio.id != null) return false;
        if (authors != null ? !authors.equals(biblio.authors) : biblio.authors != null) return false;
        if (bugsReference != null ? !bugsReference.equals(biblio.bugsReference) : biblio.bugsReference != null) return false;
        if (fullReference != null ? !fullReference.equals(biblio.fullReference) : biblio.fullReference != null)
            return false;
        return title != null ? title.equals(biblio.title) : biblio.title == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (authors != null ? authors.hashCode() : 0);
        result = 31 * result + (bugsReference != null ? bugsReference.hashCode() : 0);
        result = 31 * result + (fullReference != null ? fullReference.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        return result;
    }

    @Override
    public String toString(){
        return "Biblio{" +
                authors + "," +
                bugsReference + "," +
                fullReference + "," +
                title + "}";
    }
}
