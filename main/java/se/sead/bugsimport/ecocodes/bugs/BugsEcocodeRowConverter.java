package se.sead.bugsimport.ecocodes.bugs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.BugsTableRowConverter;
import se.sead.bugsimport.ecocodes.bugs.bugsmodel.EcoBugs;
import se.sead.bugsimport.ecocodes.bugs.bugsmodel.EcoBugsBugsTable;
import se.sead.bugsimport.ecocodes.seadmodel.Ecocode;
import se.sead.bugsimport.tracing.SeadDataFromTraceHelper;
import se.sead.repositories.EcocodeRepository;

@Component
public class BugsEcocodeRowConverter implements BugsTableRowConverter<EcoBugs, Ecocode> {

    @Autowired
    private BugsEcocodesTraceHelper traceHelper;
    @Autowired
    private BugsEcocodeCreator creator;


    @Override
    public Ecocode convertForDataRow(EcoBugs bugsData) {
        Ecocode fromLastTrace = traceHelper.getFromLastTrace(bugsData.compressToString());
        if(fromLastTrace == null){
            return create(bugsData);
        } else{
            return fromLastTrace;
        }
    }

    private Ecocode create(EcoBugs bugsData){
        return creator.create(bugsData);
    }

    @Component
    public static class BugsEcocodesTraceHelper extends SeadDataFromTraceHelper<EcoBugs, Ecocode> {

        @Autowired
        public BugsEcocodesTraceHelper(EcocodeRepository repository){
            super(EcoBugsBugsTable.TABLE_NAME, "tbl_ecocodes", true, repository);
        }
    }
}
