package se.sead.speciesbiology;

import se.sead.bugsimport.species.seadmodel.*;
import se.sead.model.TestTaxaAuthor;
import se.sead.model.TestTaxaFamily;
import se.sead.model.TestTaxaGenus;
import se.sead.model.TestTaxaSpecies;
import se.sead.util.TestSpeciesTreeBuilder;

import java.util.HashMap;
import java.util.Map;

class SpeciesBiologyTestSpeciesTreeBuiler implements TestSpeciesTreeBuilder {

    private TaxaFamily carabidae;
    private TaxaGenus cicindela;
    private TaxaAuthor linneus;
    private TaxaAuthor dej;
    Map<String, TaxaSpecies> species = new HashMap<>(6);

    SpeciesBiologyTestSpeciesTreeBuiler(TaxaOrder defaultOrder){
        createSpecies(defaultOrder);
    }

    private void createSpecies(TaxaOrder defaultOrder){
        carabidae = TestTaxaFamily.create(1, "CARABIDAE", defaultOrder);
        cicindela = TestTaxaGenus.create(2, "Cicindela", carabidae);
        linneus = TestTaxaAuthor.create(1, "L.");
        dej = TestTaxaAuthor.create(2, "Dej.");
        createExistingSpecies();
        createNewSpecies(defaultOrder);
    }

    private void createExistingSpecies() {
        species.put("10010020", TestTaxaSpecies.create(2, "sylvatica", cicindela, linneus));
        species.put("10010050", TestTaxaSpecies.create(3, "hybrida", cicindela, linneus));
        species.put("10010060", TestTaxaSpecies.create(4, "maritima", cicindela, dej));
    }

    private void createNewSpecies(TaxaOrder defaultOrder){
        species.put("10010070", TestTaxaSpecies.create(null, "campestris", cicindela, linneus));
        TaxaGenus cylindera = TestTaxaGenus.create(null, "Cylindera", carabidae);
        species.put("10010080", TestTaxaSpecies.create(null, "germanica", cylindera, linneus));
        species.put("9999", TestTaxaSpecies.create(null,
                "No data",
                TestTaxaGenus.create(null, "No data", TestTaxaFamily.create(null, "None", defaultOrder)),
                null
        ));
    }

    @Override
    public Map<String, TaxaSpecies> buildRepository() {
        return species;
    }
}
