package se.sead.bugsimport.speciessynonyms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.sead.bugsimport.BugsSeadMapper;
import se.sead.bugsimport.Importer;
import se.sead.bugsimport.species.IndexImporter;
import se.sead.bugsimport.speciesassociation.seadmodel.SpeciesAssociation;
import se.sead.bugsimport.speciessynonyms.bugsmodel.Synonym;

@Service
public class SynonymImporter extends Importer<Synonym, SpeciesAssociation> {

    private SynonymPersister synonymPersister;

    @Autowired
    public SynonymImporter(
            BugsSeadMapper<Synonym, SpeciesAssociation> dataMapper,
            SynonymPersister persister,
            IndexImporter indexImporter) {
        super(dataMapper, persister, indexImporter);
        this.synonymPersister = persister;
    }

    @Override
    public void run() {
        synonymPersister.init();
        super.run();
        synonymPersister.clear();
    }
}
