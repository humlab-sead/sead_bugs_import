package se.sead.model;

import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.bugsimport.species.seadmodel.TaxonomicOrder;
import se.sead.bugsimport.species.seadmodel.TaxonomicOrderSystem;

import java.math.BigDecimal;

/**
 * Created by erer0001 on 2016-04-28.
 */
public class TestTaxonomyOrder extends TaxonomicOrder {


    public static TestTaxonomyOrder create(Integer testId, TaxaSpecies species, BigDecimal code, TaxonomicOrderSystem orderSystem){
        TestTaxonomyOrder taxonomyOrder = new TestTaxonomyOrder(testId);
        taxonomyOrder.setSpecies(species);
        taxonomyOrder.setOrderSystem(orderSystem);
        taxonomyOrder.setCode(convertToSeadDatabasScale(code));
        return taxonomyOrder;
    }

    private static BigDecimal convertToSeadDatabasScale(BigDecimal original){
        return original != null ? original.setScale(10) : null;
    }

    TestTaxonomyOrder(Integer testId) {
        super.setId(testId);
    }
}
