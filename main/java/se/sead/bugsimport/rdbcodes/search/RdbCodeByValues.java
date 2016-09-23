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

    private final RdbCode ERROR;

    @Autowired
    private RdbCodeRepository repository;
    @Autowired
    private RdbSystemFromTrace systemFromTraceHelper;

    public RdbCodeByValues(){
        ERROR = new RdbCode();
        ERROR.addError("Sead data matches values");
    }

    @Override
    public RdbCode get(BugsRDBCodes bugsData) {
        RdbSystem systemFromTrace = systemFromTraceHelper.getFromLastTrace(String.valueOf(bugsData.getRdbSystemCode()));
        if(systemFromTrace == null || !systemFromTrace.isErrorFree()){
            return NO_CODE_FOUND;
        }
        List<RdbCode> foundRdbCodes = repository.findByCategoryAndDefinitionAndSystem(bugsData.getCategory(), bugsData.getRdbDefinition(), systemFromTrace);
        if(!foundRdbCodes.isEmpty()){
            return ERROR;
        } else {
            return NO_CODE_FOUND;
        }
    }
}
