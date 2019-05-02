package se.sead.bugsimport.mcr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.sead.bugsimport.Importer;
import se.sead.bugsimport.mcr.bugsmodel.BirmBeetleDat;
import se.sead.bugsimport.mcr.seadmodel.BirmBeetleData;
import se.sead.bugsimport.species.IndexImporter;

@Service
public class BirmBeetleDataImporter extends Importer<BirmBeetleDat, BirmBeetleData> {

    @Autowired
    public BirmBeetleDataImporter(
            BirmBeetleToMCRMapper dataMapper,
            BirmBeetleDataPersister persister,
            IndexImporter indexImporter) {
        super(dataMapper, persister, indexImporter);
    }
}
