package se.sead.bugsimport.attributes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.Persister;
import se.sead.bugsimport.attributes.bugsmodel.BugsAttributes;
import se.sead.bugsimport.attributes.seadmodel.TaxaMeasuredAttributes;
import se.sead.repositories.MeasuredAttributesRepository;

@Component
public class TaxaMeasuredAttributesPersister extends Persister<BugsAttributes, TaxaMeasuredAttributes> {

    @Autowired
    private MeasuredAttributesRepository repository;

    @Override
    protected TaxaMeasuredAttributes save(TaxaMeasuredAttributes seadValue) {
        return repository.saveOrUpdate(seadValue);
    }
}
