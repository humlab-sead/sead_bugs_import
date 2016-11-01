package se.sead.bugsimport.ecocodedefinition.bugs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.sead.bugsimport.BugsSeadMapper;
import se.sead.bugsimport.Importer;
import se.sead.bugsimport.ecocodedefinition.bugs.bugsmodel.EcoDefBugs;
import se.sead.bugsimport.ecocodedefinition.seadmodel.EcocodeDefinition;
import se.sead.bugsimport.ecocodedefinitiongroups.EcocodeGroupImporter;

@Service
public class BugsDefinitionImporter extends Importer<EcoDefBugs, EcocodeDefinition> {

    @Autowired
    public BugsDefinitionImporter(
            BugsDefinitionBugsSeadMapper dataMapper,
            BugsDefinitionPersister persister,
            EcocodeGroupImporter ecocodeGroupImporter) {
        super(dataMapper, persister, ecocodeGroupImporter);
    }
}
