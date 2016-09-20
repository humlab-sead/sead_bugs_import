package se.sead.bugsimport.rdbsystems.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.rdbsystems.bugsmodel.BugsRDBSystem;
import se.sead.bugsimport.rdbsystems.bugsmodel.RDBSystemBugsTable;
import se.sead.bugsimport.rdbsystems.seadmodel.RdbSystem;
import se.sead.bugsimport.tracing.SeadDataFromTraceHelper;
import se.sead.repositories.RDBSystemRepository;

@Component
public class RdbSystemFromTrace extends SeadDataFromTraceHelper<BugsRDBSystem, RdbSystem> {

    @Autowired
    public RdbSystemFromTrace(RDBSystemRepository rdbSystemRepository) {
        super(RDBSystemBugsTable.TABLE_NAME, false, rdbSystemRepository);
    }
}
