package se.sead.bugsimport.bibliography;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.BugsTableRowConverter;
import se.sead.bugsimport.bibliography.bugsmodel.BugsBiblio;
import se.sead.bugsimport.bibliography.bugsmodel.BugsBiblioBugsTable;
import se.sead.bugsimport.bibliography.seadmodel.Biblio;
import se.sead.bugsimport.tracing.SeadDataFromTraceHelper;
import se.sead.repositories.BiblioDataRepository;

@Component
public class BibliographyRowConverter implements BugsTableRowConverter<BugsBiblio, Biblio> {

    private BiblioTraceHelper traceHelper;

    @Autowired
    public BibliographyRowConverter(BiblioTraceHelper traceHelper){
        this.traceHelper = traceHelper;
    }

    @Override
    public Biblio convertForDataRow(BugsBiblio bugsData) {
        Biblio fromLastTrace = traceHelper.getFromLastTrace(bugsData.getBugsIdentifier());
        if(fromLastTrace == null){
            return create(bugsData);
        } else if(fromLastTrace.isErrorFree()){
            return update(fromLastTrace, bugsData);
        }
        return fromLastTrace;
    }

    private Biblio create(BugsBiblio bugsData){
        return update(new Biblio(), bugsData);
    }

    private Biblio update(Biblio biblio, BugsBiblio bugsBiblio){
        new BiblioUpdater(bugsBiblio, biblio).update();
        return biblio;
    }

    @Component
    public static class BiblioTraceHelper extends SeadDataFromTraceHelper<BugsBiblio, Biblio> {
        public BiblioTraceHelper(BiblioDataRepository accessRepository) {
            super(BugsBiblioBugsTable.BUGS_TABLE_NAME, false, accessRepository);
        }
    }
}
