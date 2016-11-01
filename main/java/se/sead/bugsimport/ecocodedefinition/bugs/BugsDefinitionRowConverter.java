package se.sead.bugsimport.ecocodedefinition.bugs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.BugsTableRowConverter;
import se.sead.bugsimport.ecocodedefinition.bugs.bugsmodel.EcoDefBugs;
import se.sead.bugsimport.ecocodedefinition.bugs.bugsmodel.EcoDefBugsBugsTable;
import se.sead.bugsimport.ecocodedefinition.seadmodel.EcocodeDefinition;
import se.sead.bugsimport.tracing.SeadDataFromTraceHelper;
import se.sead.repositories.EcocodeDefinitionRepository;

@Component
public class BugsDefinitionRowConverter implements BugsTableRowConverter<EcoDefBugs, EcocodeDefinition> {

    @Autowired
    private BugsDefinitionTraceHelper traceHelper;
    @Autowired
    private BugsDefinitionUpdater updater;

    @Override
    public EcocodeDefinition convertForDataRow(EcoDefBugs bugsData) {
        EcocodeDefinition fromLastTrace = traceHelper.getFromLastTrace(bugsData.getBugsIdentifier());
        if(fromLastTrace == null){
            return create(bugsData);
        } else if(fromLastTrace.isErrorFree()){
            return update(fromLastTrace, bugsData);
        } else {
            return fromLastTrace;
        }
    }

    private EcocodeDefinition create(EcoDefBugs bugsData){
        return update(new EcocodeDefinition(), bugsData);
    }

    private EcocodeDefinition update(EcocodeDefinition original, EcoDefBugs bugsData){
        updater.update(original, bugsData);
        return original;
    }


    @Component
    public static class BugsDefinitionTraceHelper extends SeadDataFromTraceHelper<EcoDefBugs, EcocodeDefinition> {

        @Autowired
        public BugsDefinitionTraceHelper(EcocodeDefinitionRepository repository){
            super(EcoDefBugsBugsTable.TABLE_NAME, "tbl_ecocode_definitions", false, repository);
        }
    }
}
