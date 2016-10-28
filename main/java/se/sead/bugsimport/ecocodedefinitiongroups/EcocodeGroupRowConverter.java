package se.sead.bugsimport.ecocodedefinitiongroups;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.BugsTableRowConverter;
import se.sead.bugsimport.ecocodedefinitiongroups.bugsmodel.EcoDefGroups;
import se.sead.bugsimport.ecocodedefinitiongroups.bugsmodel.EcoDefGroupsBugsTable;
import se.sead.bugsimport.ecocodedefinitiongroups.converters.EcocodeGroupUpdater;
import se.sead.bugsimport.ecocodedefinitiongroups.seadmodel.EcocodeGroup;
import se.sead.bugsimport.tracing.SeadDataFromTraceHelper;
import se.sead.repositories.EcocodeGroupRepository;

@Component
public class EcocodeGroupRowConverter implements BugsTableRowConverter<EcoDefGroups, EcocodeGroup> {

    @Autowired
    private EcocodeGroupTraceHelper traceHelper;
    @Autowired
    private EcocodeGroupUpdater updater;

    @Override
    public EcocodeGroup convertForDataRow(EcoDefGroups bugsData) {
        EcocodeGroup fromLastTrace = traceHelper.getFromLastTrace(bugsData.getEcoGroupCode());
        if(fromLastTrace == null){
            return create(bugsData);
        } else if(fromLastTrace.isErrorFree()){
            return update(fromLastTrace, bugsData);
        } else {
            return fromLastTrace;
        }
    }

    private EcocodeGroup create(EcoDefGroups bugsData){
        return update(new EcocodeGroup(), bugsData);
    }

    private EcocodeGroup update(EcocodeGroup original, EcoDefGroups bugsData){
        updater.update(original, bugsData);
        return original;
    }

    @Component
    public static class EcocodeGroupTraceHelper extends SeadDataFromTraceHelper<EcoDefGroups, EcocodeGroup> {
        @Autowired
        public EcocodeGroupTraceHelper(EcocodeGroupRepository repository){
            super(EcoDefGroupsBugsTable.TABLE_NAME, "tbl_ecocode_groups", false, repository);
        }
    }
}
