package se.sead.bugsimport.speciesbiology;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.sead.bugsimport.Importer;
import se.sead.bugsimport.species.IndexImporter;
import se.sead.bugsimport.speciesbiology.bugsmodel.Biology;
import se.sead.bugsimport.speciesbiology.seadmodel.TextBiology;

/**
 * Created by erer0001 on 2016-05-18.
 */
@Service
public class TextBiologyImporter extends Importer<Biology, TextBiology> {

    @Autowired
    public TextBiologyImporter(
            TextBiologyMapper dataMapper,
            BiologyPersister persister,
            IndexImporter indexImporter) {
        super(dataMapper, persister, indexImporter);
    }
}
