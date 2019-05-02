package se.sead.bugsimport.attributes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.sead.bugsimport.Importer;
import se.sead.bugsimport.attributes.bugsmodel.BugsAttributes;
import se.sead.bugsimport.attributes.seadmodel.TaxaMeasuredAttributes;
import se.sead.bugsimport.species.IndexImporter;

@Service
public class TaxaMeasuredAttributesImporter extends Importer<BugsAttributes, TaxaMeasuredAttributes>{

    @Autowired
    public TaxaMeasuredAttributesImporter(
        TaxaMeasuredAttributesBugsSeadMapper dataMapper,
        TaxaMeasuredAttributesPersister persister,
        IndexImporter indexImporter
    ){
        super(dataMapper, persister, indexImporter);
    }
}
