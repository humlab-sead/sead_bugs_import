package se.sead.speciesdistribution;

import se.sead.bugsimport.species.seadmodel.*;
import se.sead.model.TestTaxaAuthor;
import se.sead.model.TestTaxaFamily;
import se.sead.model.TestTaxaGenus;
import se.sead.model.TestTaxaSpecies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpeciesBuilder {

    private Map<String, TaxaSpecies> species;
    private TaxaGenus cicindela;

    SpeciesBuilder(TaxaOrder defaultOrder){
        cicindela = TestTaxaGenus.create(2, "Cicindela", TestTaxaFamily.create(1, "CARABIDAE", defaultOrder));
        species = new HashMap<>();
        addExistingSpecies();
        addCreatedSpecies();
    }

    private void addExistingSpecies() {
        TaxaAuthor l = TestTaxaAuthor.create(1, "L.");
        species.put("01.0010020", TestTaxaSpecies.create(2, "sylvatica", cicindela, l));
        species.put("01.0010050", TestTaxaSpecies.create(3, "hybrida", cicindela, l));
    }

    private void addCreatedSpecies() {
        species.put("01.0010060",
                TestTaxaSpecies.create(
                        null,
                        "maritima",
                        cicindela,
                        TestTaxaAuthor.create(null, "Dej.")
                ));
    }

    List<TaxaSpecies> getSpecies(){
        return new ArrayList<>(species.values());
    }

    TaxaSpecies getSpecies(String bugsCode){
        if(species.containsKey(bugsCode)){
            return species.get(bugsCode);
        }
        throw new UnsupportedOperationException(bugsCode);
    }
}
