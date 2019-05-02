package se.sead.bugsimport.fossil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.BugsTableRowConverter;
import se.sead.bugsimport.fossil.bugsmodel.Fossil;
import se.sead.bugsimport.fossil.converters.AbundanceUpdater;
import se.sead.bugsimport.fossil.converters.FossilTraceHelper;
import se.sead.bugsimport.fossil.seadmodel.Abundance;

@Component
public class FossilRowConverter implements BugsTableRowConverter<Fossil, Abundance> {

    @Autowired
    private FossilTraceHelper traceHelper;
    @Autowired
    private AbundanceUpdater updater;

    @Override
    public Abundance convertForDataRow(Fossil bugsData) {
        Abundance fromLastTrace = traceHelper.getFromLastTrace(bugsData.getBugsIdentifier());
        if(fromLastTrace == null){
            return create(bugsData);
        } else if(fromLastTrace.isErrorFree()){
            return update(fromLastTrace, bugsData);
        } else {
            return fromLastTrace;
        }
    }

    private Abundance create(Fossil bugsData){
        return update(new Abundance(), bugsData);
    }

    private Abundance update(Abundance original, Fossil bugsData){
        updater.update(original, bugsData);
        return original;
    }
}
