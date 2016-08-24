package se.sead.repositories;

import se.sead.bugsimport.locations.seadmodel.Location;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.bugsimport.taxaseasonality.seadmodel.ActivityType;
import se.sead.bugsimport.taxaseasonality.seadmodel.Season;
import se.sead.bugsimport.taxaseasonality.seadmodel.TaxaSeasonality;

import java.util.List;

public interface TaxaSeasonalityRepository extends CreateAndReadRepository<TaxaSeasonality, Integer> {
    List<TaxaSeasonality> findAllBySpecies(TaxaSpecies species);
    TaxaSeasonality findByLocationAndTypeAndSpeciesAndSeason(Location location, ActivityType type, TaxaSpecies species, Season season);
    List<TaxaSeasonality> findAll();
}
