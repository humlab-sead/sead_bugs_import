package se.sead.bugsimport.rdbcodes.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.rdbcodes.bugsmodel.BugsRDBCodes;
import se.sead.bugsimport.rdbcodes.seadmodel.RdbCode;
import se.sead.bugsimport.rdbsystems.converters.RdbSystemFromTrace;
import se.sead.bugsimport.rdbsystems.seadmodel.RdbSystem;
import se.sead.repositories.RdbCodeRepository;

import java.util.List;

@Component
public class RdbCodeByValues implements SearchStrategy {

    @Autowired
    private RdbCodeRepository repository;
    @Autowired
    private RdbSystemFromTrace systemFromTraceHelper;

    @Override
    public RdbCode get(BugsRDBCodes bugsData) {
        RdbSystem systemFromTrace = systemFromTraceHelper.getFromLastTrace(String.valueOf(bugsData.getRdbSystemCode()));
        if(systemFromTrace == null || !systemFromTrace.isErrorFree()){
            return NO_CODE_FOUND;
        }
        List<RdbCode> foundRdbCodes = repository.findByCategoryAndDefinitionAndSystem(bugsData.getCategory(), bugsData.getRdbDefinition(), systemFromTrace);
        if(foundRdbCodes.size() == 1){
            return foundRdbCodes.get(0);
        } else {
            return NO_CODE_FOUND;
        }
    }
}
