package se.sead.bugsimport.attributes.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.attributes.bugsmodel.BugsAttributes;
import se.sead.bugsimport.attributes.seadmodel.TaxaMeasuredAttributes;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.repositories.MeasuredAttributesRepository;
import se.sead.repositories.TaxonomicOrderRepository;

@Component
@Order(3)
public class TaxaMeasuredAttributesByValueSearch implements TaxaMeasuredAttributesSearch {

    @Autowired
    private MeasuredAttributesRepository repository;
    @Autowired
    private TaxonomicOrderRepository taxonomicOrderRepository;

    @Override
    public TaxaMeasuredAttributes findFor(BugsAttributes bugsData) {
        TaxaSpecies bugsSpeciesByCode = taxonomicOrderRepository.findBugsSpeciesByCode(bugsData.getCode());
        TaxaMeasuredAttributes found = repository.findByTypeAndMeasureAndUnitsAndSpecies(bugsData.getType(), bugsData.getMeasure(), bugsData.getUnits(), bugsSpeciesByCode);
        if(found == null){
            return NO_ATTRIBUTES_FOUND;
        }
        return found;
    }


}
