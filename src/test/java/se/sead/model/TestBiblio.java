package se.sead.model;

import se.sead.bugsimport.bibliography.seadmodel.Biblio;

public class TestBiblio extends Biblio {

    private TestBiblio(Integer id) {
        setId(id);
    }

    public static Biblio create(Integer id, String bugsReference, String authors, String title) {
        Biblio biblio = new TestBiblio(id);
        biblio.setBugsReference(bugsReference);
        biblio.setAuthors(authors);
        biblio.setTitle(title);
        biblio.setFullReference(authors == null || title == null ? bugsReference : authors + " " + title);
        return biblio;
    }
}
