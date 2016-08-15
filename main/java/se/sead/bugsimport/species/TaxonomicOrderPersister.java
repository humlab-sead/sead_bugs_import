package se.sead.bugsimport.species;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.Persister;
import se.sead.bugsimport.species.bugsmodel.INDEX;
import se.sead.bugsimport.species.seadmodel.TaxonomicOrder;
import se.sead.repositories.TaxonomicOrderRepository;

/**
 * Created by erer0001 on 2016-04-28.
 */
@Component
public class TaxonomicOrderPersister extends Persister<INDEX, TaxonomicOrder> {

    @Autowired
    private TaxonomicOrderRepository taxonomicOrderRepository;

    @Override
    protected TaxonomicOrder save(TaxonomicOrder seadValue) {
        return taxonomicOrderRepository.saveOrUpdate(seadValue);
    }
}
