package se.sead.bugsimport.taxanotes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.Persister;
import se.sead.bugsimport.taxanotes.bugsmodel.TaxoNotes;
import se.sead.bugsimport.taxanotes.seadmodel.TaxonomicNotes;
import se.sead.repositories.TaxonomicNotesRepository;

@Component
public class TaxonomicNotesPersister extends Persister<TaxoNotes, TaxonomicNotes> {

    @Autowired
    private TaxonomicNotesRepository repository;

    @Override
    protected TaxonomicNotes save(TaxonomicNotes seadValue) {
        return repository.saveOrUpdate(seadValue);
    }
}
