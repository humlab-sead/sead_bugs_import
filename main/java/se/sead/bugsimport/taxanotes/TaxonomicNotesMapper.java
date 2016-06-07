package se.sead.bugsimport.taxanotes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugs.AccessReaderProvider;
import se.sead.bugsimport.BugsSeadMapper;
import se.sead.bugsimport.taxanotes.bugsmodel.TaxoNotes;
import se.sead.bugsimport.taxanotes.bugsmodel.TaxoNotesBugsTable;
import se.sead.bugsimport.taxanotes.seadmodel.TaxonomicNotes;

@Component
public class TaxonomicNotesMapper extends BugsSeadMapper<TaxoNotes, TaxonomicNotes> {

    @Autowired
    public TaxonomicNotesMapper(
            AccessReaderProvider accessReaderProvider,
            TaxoNotesTableRowConverter singleBugsTableRowConverterForMapper) {
        super(
                accessReaderProvider.getReader(),
                new TaxoNotesBugsTable(),
                singleBugsTableRowConverterForMapper
        );
    }
}
