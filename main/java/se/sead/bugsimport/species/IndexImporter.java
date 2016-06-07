package se.sead.bugsimport.species;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.sead.bugsimport.species.bugsmodel.INDEX;
import se.sead.bugsimport.Importer;
import se.sead.bugsimport.BugsSeadMapper;
import se.sead.bugsimport.species.seadmodel.TaxonomicOrder;

/**
 * Created by erer0001 on 2016-05-13.
 */
@Service
public class IndexImporter extends Importer<INDEX, TaxonomicOrder> {

    @Autowired
    public IndexImporter(
            TaxonomicOrderIndexMapper dataMapper,
            TaxonomicOrderPersister persister) {
        super(dataMapper, persister);
    }
}
