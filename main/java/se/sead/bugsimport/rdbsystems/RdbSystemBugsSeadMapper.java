package se.sead.bugsimport.rdbsystems;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.BugsSeadMapper;
import se.sead.bugsimport.rdbsystems.bugsmodel.BugsRDBSystem;
import se.sead.bugsimport.rdbsystems.bugsmodel.RDBSystemBugsTable;
import se.sead.bugsimport.rdbsystems.seadmodel.RdbSystem;

@Component
public class RdbSystemBugsSeadMapper extends BugsSeadMapper<BugsRDBSystem, RdbSystem> {

    @Autowired
    public RdbSystemBugsSeadMapper(
            RdbSystemRowConverter singleBugsTableRowConverterForMapper) {
        super(new RDBSystemBugsTable(), singleBugsTableRowConverterForMapper);
    }
}
