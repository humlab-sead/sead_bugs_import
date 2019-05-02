package se.sead.bugsimport.bibliography;

import se.sead.bugsimport.bibliography.bugsmodel.BugsBiblio;
import se.sead.bugsimport.bibliography.seadmodel.Biblio;

import java.util.Objects;

class BiblioUpdater {

    private BugsBiblio bugsData;
    private Biblio original;

    BiblioUpdater(BugsBiblio bugsData, Biblio original) {
        this.bugsData = bugsData;
        this.original = original;
    }

    void update(){
        boolean updated = setAuthor();
        updated = setReference() || updated;
        updated = setTitle() || updated;
        if(updated){
            original.setUpdated(true);
        }
    }

    private boolean setAuthor(){
        String originalAuthor = original.getBugsAuthor();
        original.setBugsAuthor(bugsData.getAuthor());
        return !Objects.equals(originalAuthor, original.getBugsAuthor());
    }

    private boolean setReference(){
        String originalReference = original.getBugsReference();
        original.setBugsReference(bugsData.getReference());
        return !Objects.equals(originalReference, original.getBugsReference());
    }

    private boolean setTitle(){
        String originalTitle = original.getBugsTitle();
        original.setBugsTitle(bugsData.getTitle());
        return !Objects.equals(originalTitle, original.getBugsTitle());
    }
}
