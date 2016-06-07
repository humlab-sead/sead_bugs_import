package se.sead.model;

import se.sead.bugsimport.species.seadmodel.TaxonomicOrderSystem;

/**
 * Created by erer0001 on 2016-05-10.
 */
public class TestTaxonomicOrderSystem extends TaxonomicOrderSystem {

    private TestTaxonomicOrderSystem(Integer id){
        super.setId(id);
    }

    public static TaxonomicOrderSystem create(Integer id, String orderSystemName){
        TaxonomicOrderSystem system = new TestTaxonomicOrderSystem(id);
        system.setSystemName(orderSystemName);
        return system;
    }
}
