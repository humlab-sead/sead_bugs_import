package se.sead.repositories;

import org.springframework.data.jpa.repository.Query;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.bugsimport.species.seadmodel.TaxaGenus;

/**
 * Created by erer0001 on 2016-04-22.
 */
public interface SpeciesRepository extends CreateAndReadRepository<TaxaSpecies, Integer> {
    @Query("select species from TaxaSpecies species " +
            "where lower(species.speciesName) = lower(?1) " +
            "and lower(species.genus.genusName) = lower(?2)")
    TaxaSpecies findByName(String speciesName, String genusName);
    TaxaSpecies findBySpeciesNameAndGenus(String speciesName, TaxaGenus genus);
}
