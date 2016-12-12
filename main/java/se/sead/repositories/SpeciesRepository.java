package se.sead.repositories;

import org.springframework.data.jpa.repository.Query;
import se.sead.bugsimport.species.seadmodel.TaxaGenus;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;

import java.util.List;

public interface SpeciesRepository extends CreateAndReadRepository<TaxaSpecies, Integer> {
    @Query("select species from TaxaSpecies species " +
            "where lower(species.speciesName) = lower(?1) " +
            "and lower(species.genus.genusName) = lower(?2)")
    TaxaSpecies findByName(String speciesName, String genusName);

    TaxaSpecies findBySpeciesNameAndGenusGenusNameAndAuthorAuthorName(String speciesName, String genusName, String authorName);

    @Query("select species from TaxaSpecies species " +
            "where species.speciesName = 'No data' and " +
            "species.genus.genusName = 'No data' and " +
            "species.genus.family.order.orderName = 'ORDER PENDING CLASSIFICATION'")
    TaxaSpecies getBugsNoDataSpecies();

    List<TaxaSpecies> findAll();
}
