package se.sead.bugsimport.bibliography;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.BugsSeadMapper;
import se.sead.bugsimport.bibliography.bugsmodel.BugsBiblio;
import se.sead.bugsimport.bibliography.bugsmodel.BugsBiblioBugsTable;
import se.sead.bugsimport.bibliography.seadmodel.Biblio;

@Component
public class BibliographyBugsSeadMapper extends BugsSeadMapper<BugsBiblio, Biblio> {

    @Autowired
    public BibliographyBugsSeadMapper(BibliographyRowConverter singleBugsTableRowConverterForMapper) {
        super(new BugsBiblioBugsTable(), singleBugsTableRowConverterForMapper);
    }
}
