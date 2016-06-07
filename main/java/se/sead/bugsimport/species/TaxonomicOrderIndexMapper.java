package se.sead.bugsimport.species;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import se.sead.bugs.AccessReaderProvider;
import se.sead.bugsimport.species.bugsmodel.INDEX;
import se.sead.bugsimport.species.bugsmodel.INDEXBugsTable;
import se.sead.bugsimport.BugsSeadMapper;
import se.sead.bugsimport.species.seadmodel.TaxonomicOrder;

/**
 * Created by erer0001 on 2016-04-22.
 */
@Component
public class TaxonomicOrderIndexMapper extends BugsSeadMapper<INDEX, TaxonomicOrder> {

    @Autowired
    public TaxonomicOrderIndexMapper(
            AccessReaderProvider databaseReader,
            INDEXtoTaxonomicOrderRowConverter indexToTaxonomicOrderMappings){
        super(databaseReader.getReader(), new INDEXBugsTable(), indexToTaxonomicOrderMappings);
    }
}
