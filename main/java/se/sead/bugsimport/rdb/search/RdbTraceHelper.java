package se.sead.bugsimport.rdb.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.rdb.bugsmodel.BugsRDB;
import se.sead.bugsimport.rdb.bugsmodel.RDBBugsTable;
import se.sead.bugsimport.rdb.seadmodel.Rdb;
import se.sead.bugsimport.tracing.SeadDataFromTraceHelper;
import se.sead.repositories.RdbRepository;

@Component
public class RdbTraceHelper extends SeadDataFromTraceHelper<BugsRDB, Rdb> {

    @Autowired
    public RdbTraceHelper(RdbRepository accessRepository) {
        super(RDBBugsTable.TABLE_NAME, true, accessRepository);
    }


}
