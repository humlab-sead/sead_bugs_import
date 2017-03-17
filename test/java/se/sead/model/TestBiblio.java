package se.sead.model;

import se.sead.bugsimport.bibliography.seadmodel.Biblio;

public class TestBiblio extends Biblio {

    private TestBiblio(Integer id){
        super.setId(id);
    }

    public static Biblio create(Integer id, String bugsAuthor, String bugsReference, String bugsTitle){
        Biblio biblio = new TestBiblio(id);
        biblio.setBugsAuthor(bugsAuthor);
        biblio.setBugsReference(bugsReference);
        biblio.setBugsTitle(bugsTitle);
        return biblio;
    }
}
