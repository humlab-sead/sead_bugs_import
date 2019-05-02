package se.sead.bugsimport.bibliography;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.sead.bugsimport.BugsSeadMapper;
import se.sead.bugsimport.Importer;
import se.sead.bugsimport.Persister;
import se.sead.bugsimport.bibliography.bugsmodel.BugsBiblio;
import se.sead.bugsimport.bibliography.seadmodel.Biblio;

@Service
public class BibliographyImporter extends Importer<BugsBiblio, Biblio> {

    @Autowired
    protected BibliographyImporter(
            BugsSeadMapper<BugsBiblio, Biblio> dataMapper,
            BibliographyPersister persister) {
        super(dataMapper, persister);
    }
}
