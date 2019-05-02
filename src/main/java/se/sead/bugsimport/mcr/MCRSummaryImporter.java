package se.sead.bugsimport.mcr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.sead.bugsimport.Importer;
import se.sead.bugsimport.mcr.bugsmodel.MCRSummaryData;
import se.sead.bugsimport.mcr.seadmodel.MCRSummary;
import se.sead.bugsimport.species.IndexImporter;

@Service
public class MCRSummaryImporter extends Importer<MCRSummaryData, MCRSummary> {

    @Autowired
    public MCRSummaryImporter(
            MCRSummaryBugsSeadMapper dataMapper,
            MCRSummaryPersister persister,
            IndexImporter indexImporter) {
        super(dataMapper, persister, indexImporter);
    }
}
