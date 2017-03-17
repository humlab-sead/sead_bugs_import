package se.sead.bugsimport.bibliography;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.Persister;
import se.sead.bugsimport.bibliography.bugsmodel.BugsBiblio;
import se.sead.bugsimport.bibliography.seadmodel.Biblio;
import se.sead.repositories.BiblioDataRepository;

@Component
public class BibliographyPersister extends Persister<BugsBiblio, Biblio> {

    private BiblioDataRepository biblioDataRepository;

    @Autowired
    public BibliographyPersister(BiblioDataRepository biblioDataRepository) {
        this.biblioDataRepository = biblioDataRepository;
    }

    @Override
    protected Biblio save(Biblio seadValue) {
        return biblioDataRepository.saveOrUpdate(seadValue);
    }
}
