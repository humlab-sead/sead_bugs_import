package se.sead.bugsimport.rdb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.sead.bugsimport.Importer;
import se.sead.bugsimport.rdb.bugsmodel.BugsRDB;
import se.sead.bugsimport.rdb.seadmodel.Rdb;
import se.sead.bugsimport.rdbcodes.RdbCodeImporter;

@Service
public class RdbImporter extends Importer<BugsRDB, Rdb> {

    @Autowired
    public RdbImporter(
        RdbBugsSeadMapper dataMapper,
        RdbPersister persister,
        RdbCodeImporter codeImporter
    ){
        super(dataMapper, persister, codeImporter);
    }
}
