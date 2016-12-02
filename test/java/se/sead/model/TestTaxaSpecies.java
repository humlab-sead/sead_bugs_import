package se.sead.model;

import se.sead.bugsimport.species.seadmodel.TaxaAuthor;
import se.sead.bugsimport.species.seadmodel.TaxaGenus;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;

/**
 * Created by erer0001 on 2016-04-28.
 */
public class TestTaxaSpecies extends TaxaSpecies {

    TestTaxaSpecies(Integer testId) {
        super.setId(testId);
    }

    public static TestTaxaSpecies create(Integer testId, String speciesName, TaxaGenus testGenus, TaxaAuthor author){
        TestTaxaSpecies species = new TestTaxaSpecies(testId);
        species.setSpeciesName(speciesName);
        species.setGenus(testGenus);
        species.setAuthor(author);
        return species;
    }
}
