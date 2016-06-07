package se.sead.taxanotes;

import se.sead.bugsimport.species.seadmodel.*;
import se.sead.model.TestTaxaAuthor;
import se.sead.model.TestTaxaFamily;
import se.sead.model.TestTaxaGenus;
import se.sead.model.TestTaxaSpecies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class SpeciesBuilder {

    private TaxaFamily carabidae;
    private TaxaGenus cicindela;
    private TaxaAuthor linneus;
    private TaxaAuthor dej;

    private Map<String, TaxaSpecies> species;

    SpeciesBuilder(TaxaOrder defaultOrder) {
        species = new HashMap<>(6);
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
        species.put("10010050", TestTaxaSpecies.create(3, "hybrida", cicindela, linneus));
        species.put("10010060", TestTaxaSpecies.create(4, "maritima", cicindela, dej));
    }

    private void createNewSpecies(TaxaOrder defaultOrder){
        species.put("9999.0000001", TestTaxaSpecies.create(
                null,
                "No data",
                TestTaxaGenus.create(null, "No data", TestTaxaFamily.create(null, "None", defaultOrder)),
                null
        ));
    }

    TaxaSpecies getSpecies(String bugsCode){
        if(species.containsKey(bugsCode)){
            return species.get(bugsCode);
        }
        throw new UnsupportedOperationException(bugsCode);
    }

    List<TaxaSpecies> getAllSpecies(){
        return new ArrayList<>(species.values());
    }
}
