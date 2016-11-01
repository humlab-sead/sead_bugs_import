package se.sead.bugsimport.ecocodes.koch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.BugsTableRowConverter;
import se.sead.bugsimport.ecocodes.koch.bugsmodel.EcoKoch;
import se.sead.bugsimport.ecocodes.koch.bugsmodel.EcoKochBugsTable;
import se.sead.bugsimport.ecocodes.seadmodel.Ecocode;
import se.sead.bugsimport.tracing.SeadDataFromTraceHelper;
import se.sead.repositories.EcocodeRepository;

@Component
public class KochEcocodesRowConverter implements BugsTableRowConverter<EcoKoch, Ecocode> {

    @Autowired
    private KochEcocodesTraceHelper traceHelper;
    @Autowired
    private KochEcocodesCreator creator;

    @Override
    public Ecocode convertForDataRow(EcoKoch bugsData) {
        Ecocode fromLastTrace = traceHelper.getFromLastTrace(bugsData.compressToString());
        if(fromLastTrace == null){
            return create(bugsData);
        } else {
            return fromLastTrace;
        }
    }

    private Ecocode create(EcoKoch bugsData){
        return creator.create(bugsData);
    }

    @Component
    public static class KochEcocodesTraceHelper extends SeadDataFromTraceHelper<EcoKoch, Ecocode> {

        @Autowired
        public KochEcocodesTraceHelper(EcocodeRepository repository){
            super(EcoKochBugsTable.TABLE_NAME, "tbl_ecocodes", true, repository);
        }
    }
}
