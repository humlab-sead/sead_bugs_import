package se.sead.bugsimport.speciesassociation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.BugsTableRowConverter;
import se.sead.bugsimport.speciesassociation.bugsmodel.BugsSpeciesAssociation;
import se.sead.bugsimport.speciesassociation.converters.SpeciesAssociationTraceHelper;
import se.sead.bugsimport.speciesassociation.converters.SpeciesAssociationUpdater;
import se.sead.bugsimport.speciesassociation.seadmodel.SpeciesAssociation;

@Component
public class SpeciesAssociationRowConverter implements BugsTableRowConverter<BugsSpeciesAssociation, SpeciesAssociation> {

    @Autowired
    private SpeciesAssociationTraceHelper traceHelper;
    @Autowired
    private SpeciesAssociationUpdater updater;

    @Override
    public SpeciesAssociation convertForDataRow(BugsSpeciesAssociation bugsData) {
        SpeciesAssociation fromLastTrace = traceHelper.getFromLastTrace(bugsData.getBugsIdentifier());
        if(fromLastTrace == null){
            return create(bugsData);
        } else if(fromLastTrace.isErrorFree()){
            return update(fromLastTrace, bugsData);
        } else {
            return fromLastTrace;
        }
    }

    private SpeciesAssociation create(BugsSpeciesAssociation bugsData){
        return update(new SpeciesAssociation(), bugsData);
    }

    private SpeciesAssociation update(SpeciesAssociation original, BugsSpeciesAssociation bugsData){
        updater.update(original, bugsData);
        return original;
    }
}
