package se.sead.bugsimport.rdbcodes.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.rdbcodes.bugsmodel.BugsRDBCodes;
import se.sead.bugsimport.rdbcodes.bugsmodel.RDBCodesBugsTable;
import se.sead.bugsimport.rdbcodes.seadmodel.RdbCode;
import se.sead.bugsimport.tracing.SeadDataFromTraceHelper;
import se.sead.repositories.RdbCodeRepository;

@Component
public class BugsRdbCodeTraceHelper extends SeadDataFromTraceHelper<BugsRDBCodes, RdbCode>{

    @Autowired
    public BugsRdbCodeTraceHelper(RdbCodeRepository repository){
        super(RDBCodesBugsTable.TABLE_NAME, false, repository);
    }

    public RdbCode getFromLastTrace(Integer bugsRdbCodeId){
        return getFromLastTrace(String.valueOf(bugsRdbCodeId));
    }
}
