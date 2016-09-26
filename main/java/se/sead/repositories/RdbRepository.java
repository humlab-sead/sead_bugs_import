package se.sead.repositories;

import se.sead.bugsimport.locations.seadmodel.Location;
import se.sead.bugsimport.rdb.seadmodel.Rdb;
import se.sead.bugsimport.rdbcodes.seadmodel.RdbCode;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;

import java.util.List;

public interface RdbRepository extends  CreateAndReadRepository<Rdb, Integer>{
    List<Rdb> findAll();
    List<Rdb> findAllBySpecies(TaxaSpecies species);

    Rdb findByRdbCodeAndSpeciesAndCountry(RdbCode rdbCode, TaxaSpecies species, Location country);
}
