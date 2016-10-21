package se.sead.bugsimport.speciesassociation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.sead.bugsimport.BugsSeadMapper;
import se.sead.bugsimport.Importer;
import se.sead.bugsimport.species.IndexImporter;
import se.sead.bugsimport.speciesassociation.bugsmodel.BugsSpeciesAssociation;
import se.sead.bugsimport.speciesassociation.seadmodel.SpeciesAssociation;

@Service
public class SpeciesAssociationImporter extends Importer<BugsSpeciesAssociation, SpeciesAssociation> {

    @Autowired
    public SpeciesAssociationImporter(
            BugsSeadMapper<BugsSpeciesAssociation, SpeciesAssociation> dataMapper,
            SpeciesAssociationPersister persister,
            IndexImporter speciesImporter) {
        super(dataMapper, persister, speciesImporter);
    }
}
