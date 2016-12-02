package se.sead.bugsimport.species.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.species.seadmodel.TaxonomicOrder;
import se.sead.repositories.TaxonomicOrderRepository;
import se.sead.repositories.TaxonomicOrderSystemRepository;
import se.sead.utils.BigDecimalDefinition;

import java.math.BigDecimal;

@Component
public class TaxonomicOrderConverter {

    @Autowired
    private TaxonomicOrderRepository seadDataRepository;
    @Autowired
    private TaxonomicOrderSystemRepository orderSystemRepository;

    public TaxonomicOrder convertToSeadType(Double code) {
        if(code instanceof Double){
            BigDecimal codeForSeadDatabase = BigDecimalDefinition.convertToSeadCode(code);
            return getOrCreate(codeForSeadDatabase);
        }
        return null;
    }

    private TaxonomicOrder getOrCreate(BigDecimal codeForSeadDatabase) {
        TaxonomicOrder bugsCodeByCode = seadDataRepository.findBugsCodeByCode(codeForSeadDatabase);
        if(bugsCodeByCode != null){
            return bugsCodeByCode;
        } else {
            return createTaxonomicOrder(codeForSeadDatabase);
        }
    }

    private TaxonomicOrder createTaxonomicOrder(BigDecimal codeForSeadDatabase) {
        TaxonomicOrder newOrder = new TaxonomicOrder();
        newOrder.setCode(codeForSeadDatabase);
        newOrder.setOrderSystem(orderSystemRepository.getBugsSystem());
        return newOrder;
    }
}
