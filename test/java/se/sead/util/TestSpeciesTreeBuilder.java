package se.sead.util;

import se.sead.bugsimport.species.seadmodel.TaxaSpecies;

import java.util.Map;

public interface TestSpeciesTreeBuilder {
    Map<String, TaxaSpecies> buildRepository();
}
