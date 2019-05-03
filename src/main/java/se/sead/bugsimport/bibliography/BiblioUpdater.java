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
        boolean updated = setAuthors();
        updated = setBugsReference() || updated;
        updated = setFullReference() || updated;
        updated = setTitle() || updated;
        if(updated){
            original.setUpdated(true);
        }
    }

    private boolean setAuthors(){
        String originalAuthor = original.getAuthors();
        original.setAuthors(bugsData.getAuthor());
        return !Objects.equals(originalAuthor, original.getAuthors());
    }

    private boolean setBugsReference(){
        String originalBugsReference = original.getBugsReference();
        original.setBugsReference(bugsData.getReference());
        return !Objects.equals(originalBugsReference, original.getBugsReference());
    }

    private boolean setFullReference(){
        String originalFullReference = original.getFullReference();
        if (bugsData.getAuthor() == null || bugsData.getTitle() == null)
            original.setFullReference(bugsData.getReference());
        else
            original.setFullReference(bugsData.getAuthor() + " " + bugsData.getTitle());
        return !Objects.equals(originalFullReference, original.getFullReference());
    }

    private boolean setTitle(){
        String originalTitle = original.getTitle();
        original.setTitle(bugsData.getTitle());
        return !Objects.equals(originalTitle, original.getTitle());
    }
}
