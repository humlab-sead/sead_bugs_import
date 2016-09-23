package se.sead.bugsimport.rdbcodes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.BugsTableRowConverter;
import se.sead.bugsimport.rdbcodes.bugsmodel.BugsRDBCodes;
import se.sead.bugsimport.rdbcodes.converter.BugsRdbCodeTraceHelper;
import se.sead.bugsimport.rdbcodes.converter.RdbCodeUpdater;
import se.sead.bugsimport.rdbcodes.seadmodel.RdbCode;
import se.sead.bugsimport.rdbcodes.search.SearchStrategy;

import java.util.List;

import static se.sead.bugsimport.rdbcodes.search.SearchStrategy.NO_CODE_FOUND;

@Component
public class RdbCodeTableRowConverter implements BugsTableRowConverter<BugsRDBCodes, RdbCode> {

    @Autowired
    private RdbCodeUpdater codeUpdater;

    @Autowired
    private List<SearchStrategy> searchStrategies;

    @Override
    public RdbCode convertForDataRow(BugsRDBCodes bugsData) {
        RdbCode useCode = NO_CODE_FOUND;
        for (SearchStrategy searcher :
                searchStrategies) {
            useCode = searcher.get(bugsData);
            if(useCode != NO_CODE_FOUND){
                break;
            }
        }
        if(useCode == NO_CODE_FOUND){
            useCode = create(bugsData);
        } else if(useCode.isErrorFree()){
            codeUpdater.update(useCode, bugsData);
        }
        return useCode;
    }

    private RdbCode create(BugsRDBCodes bugsData) {
        return update(new RdbCode(), bugsData);
    }

    private RdbCode update(RdbCode code, BugsRDBCodes bugsData) {
        codeUpdater.update(code, bugsData);
        return code;
    }
}
