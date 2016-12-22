package se.sead.bugsimport.taxanotes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugs.AccessDatabaseProvider;
import se.sead.bugsimport.BugsSeadMapper;
import se.sead.bugsimport.taxanotes.bugsmodel.TaxoNotes;
import se.sead.bugsimport.taxanotes.bugsmodel.TaxoNotesBugsTable;
import se.sead.bugsimport.taxanotes.seadmodel.TaxonomicNotes;
import se.sead.bugsimport.translations.BugsValueTranslationService;

@Component
public class TaxonomicNotesMapper extends BugsSeadMapper<TaxoNotes, TaxonomicNotes> {

    @Autowired
    public TaxonomicNotesMapper(
            AccessDatabaseProvider accessDatabaseProvider,
            TaxoNotesTableRowConverter singleBugsTableRowConverterForMapper,
            BugsValueTranslationService dataTranslationService) {
        super(
                new TaxoNotesBugsTable(),
                singleBugsTableRowConverterForMapper
        );
    }
}
