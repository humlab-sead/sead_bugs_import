package se.sead.bugsimport.rdbcodes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.BugsSeadMapper;
import se.sead.bugsimport.rdbcodes.bugsmodel.BugsRDBCodes;
import se.sead.bugsimport.rdbcodes.bugsmodel.RDBCodesBugsTable;
import se.sead.bugsimport.rdbcodes.seadmodel.RdbCode;

@Component
public class RdbCodeBugsSeadMapper extends BugsSeadMapper<BugsRDBCodes, RdbCode> {

    @Autowired
    public RdbCodeBugsSeadMapper(
            RdbCodeTableRowConverter rowConverter) {
        super(new RDBCodesBugsTable(), rowConverter);
    }
}
