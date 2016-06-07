package se.sead.mcr;

import se.sead.bugsimport.species.seadmodel.*;
import se.sead.model.TestTaxaAuthor;
import se.sead.model.TestTaxaFamily;
import se.sead.model.TestTaxaGenus;
import se.sead.model.TestTaxaSpecies;
import se.sead.util.TestSpeciesTreeBuilder;

import java.util.HashMap;
import java.util.Map;

public class MCRSummaryTestSpeciesTreeBuilder implements TestSpeciesTreeBuilder {

    private Map<String, TaxaSpecies> species;

    MCRSummaryTestSpeciesTreeBuilder(TaxaOrder defaultOrder){
        species = new HashMap<>();
        TaxaGenus cicindela = TestTaxaGenus.create(2, "Cicindela", TestTaxaFamily.create(1, "CARABIDAE", defaultOrder));
        TaxaAuthor linneus = TestTaxaAuthor.create(1, "L.");
        species.put(
                "01.0010020",
                TestTaxaSpecies.create(2, "sylvatica", cicindela, linneus)
        );
        species.put(
                "01.0010070",
                TestTaxaSpecies.create(3, "campestris", cicindela, linneus)
        );
    }

    @Override
    public Map<String, TaxaSpecies> buildRepository() {
        return species;
    }
}
