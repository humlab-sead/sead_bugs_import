package se.sead.repositories;

import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.bugsimport.speciesdistribution.seadmodel.TextDistribution;
import se.sead.bugsimport.bibliography.seadmodel.Biblio;

import java.util.List;

public interface TextDistributionRepository extends CreateAndReadRepository<TextDistribution, Integer> {
    List<TextDistribution> findBySpecies(TaxaSpecies species);
    TextDistribution findByDistributionAndSpeciesAndReference(String distribution, TaxaSpecies species, Biblio reference);
}
