package se.sead.model;

import se.sead.bugsimport.species.seadmodel.TaxaFamily;
import se.sead.bugsimport.species.seadmodel.TaxaGenus;

/**
 * Created by erer0001 on 2016-04-28.
 */
public class TestTaxaGenus extends TaxaGenus{

    TestTaxaGenus(Integer testId){
        super.setId(testId);
    }

    public static TestTaxaGenus create(Integer testId, String genusName, TaxaFamily family){
        TestTaxaGenus testTaxaGenus = new TestTaxaGenus(testId);
        testTaxaGenus.setGenusName(genusName);
        testTaxaGenus.setFamily(family);
        return testTaxaGenus;
    }
}
