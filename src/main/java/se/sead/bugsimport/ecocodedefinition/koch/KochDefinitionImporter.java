package se.sead.bugsimport.ecocodedefinition.koch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.sead.bugsimport.Importer;
import se.sead.bugsimport.ecocodedefinition.koch.bugsmodel.EcoDefKoch;
import se.sead.bugsimport.ecocodedefinition.seadmodel.EcocodeDefinition;
import se.sead.bugsimport.ecocodedefinitiongroups.EcocodeGroupImporter;

@Service
public class KochDefinitionImporter extends Importer<EcoDefKoch, EcocodeDefinition> {

    @Autowired
    public KochDefinitionImporter(
            KochDefinitionSeadDataMapper dataMapper,
            KochDefinitionPersister persister,
            EcocodeGroupImporter groupImporter) {
        super(dataMapper, persister, groupImporter);
    }
}
