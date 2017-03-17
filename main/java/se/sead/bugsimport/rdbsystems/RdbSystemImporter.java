package se.sead.bugsimport.rdbsystems;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.sead.bugsimport.Importer;
import se.sead.bugsimport.bibliography.BibliographyImporter;
import se.sead.bugsimport.locations.country.CountryImporter;
import se.sead.bugsimport.rdbsystems.bugsmodel.BugsRDBSystem;
import se.sead.bugsimport.rdbsystems.seadmodel.RdbSystem;

@Service
public class RdbSystemImporter extends Importer<BugsRDBSystem, RdbSystem> {

    @Autowired
    public RdbSystemImporter(
            RdbSystemBugsSeadMapper dataMapper,
            RdbSystemPersister persister,
            BibliographyImporter bibliographyImporter,
            CountryImporter countryImporter) {
        super(dataMapper, persister, bibliographyImporter, countryImporter);
    }
}
