package se.sead.model;

import se.sead.bugsimport.species.seadmodel.TaxaAuthor;

/**
 * Created by erer0001 on 2016-04-28.
 */
public class TestTaxaAuthor extends TaxaAuthor {

    TestTaxaAuthor(Integer testId){
        super.setId(testId);
    }

    public static TaxaAuthor create(Integer testId, String author){
        TestTaxaAuthor taxaAuthor = new TestTaxaAuthor(testId);
        taxaAuthor.setAuthorName(author);
        return taxaAuthor;
    }
}
