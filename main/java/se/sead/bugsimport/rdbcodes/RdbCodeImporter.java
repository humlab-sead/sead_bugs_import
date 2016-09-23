package se.sead.bugsimport.rdbcodes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.sead.bugsimport.Importer;
import se.sead.bugsimport.rdbcodes.bugsmodel.BugsRDBCodes;
import se.sead.bugsimport.rdbcodes.seadmodel.RdbCode;
import se.sead.bugsimport.rdbsystems.RdbSystemImporter;

@Service
public class RdbCodeImporter extends Importer<BugsRDBCodes, RdbCode> {

    @Autowired
    public RdbCodeImporter(
            RdbCodeBugsSeadMapper dataMapper,
            RdbCodePersister persister,
            RdbSystemImporter systemImporter) {
        super(dataMapper, persister, systemImporter);
    }
}
