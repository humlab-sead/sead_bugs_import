package se.sead.bugsimport.taxanotes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.sead.bugsimport.Importer;
import se.sead.bugsimport.bibliography.BibliographyImporter;
import se.sead.bugsimport.species.IndexImporter;
import se.sead.bugsimport.taxanotes.bugsmodel.TaxoNotes;
import se.sead.bugsimport.taxanotes.seadmodel.TaxonomicNotes;

@Service
public class TaxonomicNotesImporter extends Importer<TaxoNotes, TaxonomicNotes> {

    @Autowired
    public TaxonomicNotesImporter(
            TaxonomicNotesMapper dataMapper,
            TaxonomicNotesPersister persister,
            BibliographyImporter bibliographyImporter,
            IndexImporter indexImporter) {
        super(dataMapper, persister, bibliographyImporter, indexImporter);
    }
}
