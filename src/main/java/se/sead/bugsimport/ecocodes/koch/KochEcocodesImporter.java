package se.sead.bugsimport.ecocodes.koch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.sead.bugsimport.Importer;
import se.sead.bugsimport.ecocodedefinition.koch.KochDefinitionImporter;
import se.sead.bugsimport.ecocodes.koch.bugsmodel.EcoKoch;
import se.sead.bugsimport.ecocodes.seadmodel.Ecocode;

@Service
public class KochEcocodesImporter extends Importer<EcoKoch, Ecocode> {

    @Autowired
    public KochEcocodesImporter(
            KochEcocodeBugsSeadMapper dataMapper,
            KochEcocodesPersister persister,
            KochDefinitionImporter kochDefinitionImporter) {
        super(dataMapper, persister, kochDefinitionImporter);
    }
}
