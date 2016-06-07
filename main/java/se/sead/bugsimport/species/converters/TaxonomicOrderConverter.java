package se.sead.bugsimport.species.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.species.seadmodel.TaxonomicOrder;
import se.sead.repositories.TaxonomicOrderRepository;
import se.sead.repositories.TaxonomicOrderSystemRepository;

import java.math.BigDecimal;

/**
 * Created by erer0001 on 2016-04-27.
 */
@Component
public class TaxonomicOrderConverter {

    private static final int BUGS_SCALE = 10;

    @Autowired
    private TaxonomicOrderRepository seadDataRepository;
    @Autowired
    private TaxonomicOrderSystemRepository orderSystemRepository;

    public TaxonomicOrder convertToSeadType(Double code) {
        if(code instanceof Double){
            BigDecimal codeForSeadDatabase = TaxonomicOrderConverter.convertToSeadCode(code);
            return getOrCreate(codeForSeadDatabase);
        }
        return null;
    }

    public static BigDecimal convertToSeadCode(Double code){
        return new BigDecimal(code).setScale(BUGS_SCALE, BigDecimal.ROUND_HALF_UP);
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
