package se.sead.species;

import se.sead.bugsimport.species.seadmodel.TaxaSpecies;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestSpeciesProvider {

    private Map<String, TaxaSpecies> species;

    public TestSpeciesProvider(TestSpeciesTreeBuilder testSpeciesTreeBuilder){
        species = testSpeciesTreeBuilder.buildRepository();
    }

    public TaxaSpecies getSpecies(String bugsCode){
        if(species.containsKey(bugsCode)){
            return species.get(bugsCode);
        }
        throw new UnsupportedOperationException(bugsCode);
    }

    public List<TaxaSpecies> getSpecies(){
        return new ArrayList<>(species.values());
    }
}
