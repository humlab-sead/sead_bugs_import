package se.sead.specieskeys;

import se.sead.bugsimport.species.seadmodel.TaxaFamily;
import se.sead.bugsimport.species.seadmodel.TaxaOrder;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.model.TestTaxaFamily;
import se.sead.model.TestTaxaGenus;
import se.sead.model.TestTaxaSpecies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpeciesBuilder {

    private Map<String, TaxaSpecies> species;

    private TaxaFamily carabidae;

    SpeciesBuilder(TaxaOrder defaultOrder){
        species = new HashMap<>();
        addSpecies(defaultOrder);
    }

    private void addSpecies(TaxaOrder defaultOrder) {
        setupFamilies(defaultOrder);
        addExistingSpecies();
        addCreatedSpecies();
    }

    private void setupFamilies(TaxaOrder defaultOrder) {
        carabidae = TestTaxaFamily.create(1, "CARABIDAE", defaultOrder);
    }

    private void addExistingSpecies() {
        species.put("01.00100010", TestTaxaSpecies.create(1, "index.", TestTaxaGenus.create(1, "Carabidae", carabidae), null));
        species.put("01.00101220", TestTaxaSpecies.create(2, "sp.", TestTaxaGenus.create(2, "Cicindela", carabidae), null));
    }

    private void addCreatedSpecies(){
        species.put("01.00200820", TestTaxaSpecies.create(null, "sp.", TestTaxaGenus.create(null, "Calosoma", carabidae), null));
        species.put("01.00403420", TestTaxaSpecies.create(null, "sp.", TestTaxaGenus.create(null, "Carabus", carabidae), null));
    }

    public List<TaxaSpecies> getSpecies(){
        return new ArrayList<>(species.values());
    }

    public TaxaSpecies getSpecies(String bugsCode){
        if(species.containsKey(bugsCode)){
            return species.get(bugsCode);
        }
        throw new UnsupportedOperationException(bugsCode);
    }
}
