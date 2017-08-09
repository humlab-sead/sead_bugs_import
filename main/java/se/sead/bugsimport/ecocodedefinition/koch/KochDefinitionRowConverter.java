package se.sead.bugsimport.ecocodedefinition.koch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.BugsTableRowConverter;
import se.sead.bugsimport.ecocodedefinition.koch.bugsmodel.EcoDefKoch;
import se.sead.bugsimport.ecocodedefinition.koch.bugsmodel.EcoDefKochBugsTable;
import se.sead.bugsimport.ecocodedefinition.seadmodel.EcocodeDefinition;
import se.sead.bugsimport.tracing.SeadDataFromTraceHelper;
import se.sead.bugsimport.tracing.accessors.IgnoreCaseBugsIdentifierWithSeadDataTable;
import se.sead.repositories.EcocodeDefinitionRepository;

@Component
public class KochDefinitionRowConverter implements BugsTableRowConverter<EcoDefKoch, EcocodeDefinition>{

    @Autowired
    private KochDefinitionTraceHelper traceHelper;
    @Autowired
    private KochDefinitionUpdater updater;

    @Override
    public EcocodeDefinition convertForDataRow(EcoDefKoch bugsData) {
        EcocodeDefinition fromLastTrace = traceHelper.getFromLastTrace(bugsData.getBugsIdentifier());
        if(fromLastTrace == null){
            return create(bugsData);
        } else if(fromLastTrace.isErrorFree()){
            return update(fromLastTrace, bugsData);
        } else {
            return fromLastTrace;
        }
    }

    private EcocodeDefinition create(EcoDefKoch bugsData){
        return update(new EcocodeDefinition(), bugsData);
    }

    private EcocodeDefinition update(EcocodeDefinition original, EcoDefKoch bugsData){
        updater.update(original, bugsData);
        return original;
    }

    @Component
    public static class KochDefinitionTraceHelper extends SeadDataFromTraceHelper<EcoDefKoch, EcocodeDefinition> {

        @Autowired
        public KochDefinitionTraceHelper(EcocodeDefinitionRepository repository){
            super(new IgnoreCaseBugsIdentifierWithSeadDataTable(EcoDefKochBugsTable.TABLE_NAME, "tbl_ecocode_definitions"), repository);
        }
    }
}
