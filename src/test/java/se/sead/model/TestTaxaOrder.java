package se.sead.model;

import se.sead.bugsimport.species.seadmodel.TaxaOrder;

/**
 * Created by erer0001 on 2016-05-10.
 */
public class TestTaxaOrder extends TaxaOrder {

    private TestTaxaOrder(Integer id){super.setId(id);}

    public static TaxaOrder create(Integer id, String orderName){
        TaxaOrder newOrder = new TestTaxaOrder(id);
        newOrder.setOrderName(orderName);
        return newOrder;
    }

}
