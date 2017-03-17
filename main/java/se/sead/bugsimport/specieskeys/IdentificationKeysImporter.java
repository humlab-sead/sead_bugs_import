package se.sead.bugsimport.specieskeys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.sead.bugsimport.Importer;
import se.sead.bugsimport.bibliography.BibliographyImporter;
import se.sead.bugsimport.species.IndexImporter;
import se.sead.bugsimport.specieskeys.bugsmodel.Keys;
import se.sead.bugsimport.specieskeys.seadmodel.TextIdentificationKeys;

@Service
public class IdentificationKeysImporter extends Importer<Keys, TextIdentificationKeys> {

    @Autowired
    public IdentificationKeysImporter(
            IdentificationKeysBugsSeadMapper dataMapper,
            IdentificationKeysPersister persister,
            BibliographyImporter bibliographyImporter,
            IndexImporter requiredImporters) {
        super(dataMapper, persister, bibliographyImporter, requiredImporters);
    }
}
