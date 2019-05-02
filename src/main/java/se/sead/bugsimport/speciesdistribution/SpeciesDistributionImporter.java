package se.sead.bugsimport.speciesdistribution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.sead.bugsimport.Importer;
import se.sead.bugsimport.bibliography.BibliographyImporter;
import se.sead.bugsimport.species.IndexImporter;
import se.sead.bugsimport.speciesdistribution.bugsmodel.Distrib;
import se.sead.bugsimport.speciesdistribution.seadmodel.TextDistribution;

@Service
public class SpeciesDistributionImporter extends Importer<Distrib, TextDistribution> {

    @Autowired
    public SpeciesDistributionImporter(
            SpeciesDistributionBugsSeadMapper dataMapper,
            SpeciesDistributionPersister persister,
            BibliographyImporter bibliographyImporter,
            IndexImporter indexImporter) {
        super(dataMapper, persister, bibliographyImporter, indexImporter);
    }
}
