package se.sead.repositories;

import org.springframework.data.jpa.repository.Query;
import se.sead.bugsimport.species.seadmodel.TaxaGenus;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;

public interface SpeciesRepository extends CreateAndReadRepository<TaxaSpecies, Integer> {
    @Query("select species from TaxaSpecies species " +
            "where lower(species.speciesName) = lower(?1) " +
            "and lower(species.genus.genusName) = lower(?2)")
    TaxaSpecies findByName(String speciesName, String genusName);

    TaxaSpecies findBySpeciesNameAndGenusGenusNameAndTaxaAuthorAuthorName(String speciesName, String genusName, String authorName);
}
