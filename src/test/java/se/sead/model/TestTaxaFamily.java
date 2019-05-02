package se.sead.model;

import se.sead.bugsimport.species.seadmodel.TaxaFamily;
import se.sead.bugsimport.species.seadmodel.TaxaOrder;

/**
 * Created by erer0001 on 2016-04-28.
 */
public class TestTaxaFamily extends TaxaFamily{

    TestTaxaFamily(Integer testId){
        super.setId(testId);
    }

    public static TestTaxaFamily create(Integer testId, String familyName, TaxaOrder order){
        TestTaxaFamily testTaxaFamily = new TestTaxaFamily(testId);
        testTaxaFamily.setFamilyName(familyName);
        testTaxaFamily.setOrder(order);
        return testTaxaFamily;
    }
}
