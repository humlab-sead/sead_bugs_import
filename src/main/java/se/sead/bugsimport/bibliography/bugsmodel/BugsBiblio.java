package se.sead.bugsimport.bibliography.bugsmodel;

import se.sead.bugs.TraceableBugsData;

public class BugsBiblio extends TraceableBugsData {

    private String reference;
    private String author;
    private String title;
    private String notes;

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getFullReference()
    {
        if (author == null && title == null)
            return reference;
        String fullReference = author + " " + title;
        return fullReference;
    }

    @Override
    public String compressToString() {
        return "{" +
                reference + ',' +
                author + ',' +
                title + ',' +
                notes +
                '}';
    }

    @Override
    public String getBugsIdentifier() {
        return reference;
    }

    @Override
    public String bugsTable() {
        return BugsBiblioBugsTable.BUGS_TABLE_NAME;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BugsBiblio that = (BugsBiblio) o;

        if (reference != null ? !reference.equals(that.reference) : that.reference != null) return false;
        if (author != null ? !author.equals(that.author) : that.author != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        return notes != null ? notes.equals(that.notes) : that.notes == null;
    }

    @Override
    public int hashCode() {
        int result = reference != null ? reference.hashCode() : 0;
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (notes != null ? notes.hashCode() : 0);
        return result;
    }
}
