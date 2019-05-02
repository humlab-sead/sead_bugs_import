package se.sead.bugsimport.ecocodes.bugs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.sead.bugsimport.Importer;
import se.sead.bugsimport.ecocodedefinition.bugs.BugsDefinitionImporter;
import se.sead.bugsimport.ecocodes.bugs.bugsmodel.EcoBugs;
import se.sead.bugsimport.ecocodes.seadmodel.Ecocode;

@Service
public class BugsEcocodeImporter extends Importer<EcoBugs, Ecocode> {

    @Autowired
    public BugsEcocodeImporter(
            BugsEcocodeBugsSeadMapper dataMapper,
            BugsEcocodesPersister persister,
            BugsDefinitionImporter bugsDefinitionImporter) {
        super(dataMapper, persister, bugsDefinitionImporter);
    }
}
