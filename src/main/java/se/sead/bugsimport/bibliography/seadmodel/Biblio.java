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
    @Column(name="bugs_author")
    private String bugsAuthor;
    @Column(name="bugs_reference")
    private String bugsReference;
    @Column(name="bugs_title")
    private String bugsTitle;

    @Override
    public Integer getId() {
        return id;
    }

    protected void setId(Integer id){
        this.id = id;
    }

    public String getBugsAuthor() {
        return bugsAuthor;
    }

    public void setBugsAuthor(String bugsAuthor) {
        this.bugsAuthor = bugsAuthor;
    }

    public String getBugsReference() {
        return bugsReference;
    }

    public void setBugsReference(String bugsReference) {
        this.bugsReference = bugsReference;
    }

    public String getBugsTitle() {
        return bugsTitle;
    }

    public void setBugsTitle(String bugsTitle) {
        this.bugsTitle = bugsTitle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Biblio)) return false;

        Biblio biblio = (Biblio) o;

        if (id != null ? !id.equals(biblio.id) : biblio.id != null) return false;
        if (bugsAuthor != null ? !bugsAuthor.equals(biblio.bugsAuthor) : biblio.bugsAuthor != null) return false;
        if (bugsReference != null ? !bugsReference.equals(biblio.bugsReference) : biblio.bugsReference != null)
            return false;
        return bugsTitle != null ? bugsTitle.equals(biblio.bugsTitle) : biblio.bugsTitle == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (bugsAuthor != null ? bugsAuthor.hashCode() : 0);
        result = 31 * result + (bugsReference != null ? bugsReference.hashCode() : 0);
        result = 31 * result + (bugsTitle != null ? bugsTitle.hashCode() : 0);
        return result;
    }

    @Override
    public String toString(){
        return "Biblio{" +
                bugsAuthor + "," +
                bugsReference + "," +
                bugsTitle + "}";
    }
}
