package se.sead.bugsimport.species.seadmodel;

import se.sead.sead.model.LoggableEntity;

import javax.persistence.*;

@Entity
@Table(name="tbl_taxa_tree_authors", schema = "public")
@SequenceGenerator(name="taxa_author_id_seq", sequenceName = "tbl_taxa_tree_authors_author_id_seq")
public class TaxaAuthor extends LoggableEntity {
    @Id
    @GeneratedValue(generator = "taxa_author_id_seq", strategy = GenerationType.IDENTITY)
    @Column(name="author_id", nullable = false)
    private Integer id;
    @Column(name="author_name")
    private String authorName;

    public Integer getId() {
        return id;
    }

    protected void setId(Integer id){
        this.id = id;

    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof TaxaAuthor)) return false;

        TaxaAuthor that = (TaxaAuthor) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        return authorName.equals(that.authorName);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + authorName.hashCode();
        return result;
    }
}
