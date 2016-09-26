package se.sead.bugsimport.rdb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.BugsSeadMapper;
import se.sead.bugsimport.rdb.bugsmodel.BugsRDB;
import se.sead.bugsimport.rdb.bugsmodel.RDBBugsTable;
import se.sead.bugsimport.rdb.seadmodel.Rdb;

@Component
public class RdbBugsSeadMapper extends BugsSeadMapper<BugsRDB, Rdb> {

    @Autowired
    public RdbBugsSeadMapper(
            RdbBugsRowConverter rowConverter) {
        super(new RDBBugsTable(), rowConverter);
    }
}
