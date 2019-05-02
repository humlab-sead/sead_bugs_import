package se.sead.repositories;

import se.sead.bugsimport.mcr.seadmodel.BirmBeetleData;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;

import java.util.List;

/**
 * Created by erer0001 on 2016-05-13.
 */
public interface BirmBeetleDataRepository  extends CreateAndReadRepository<BirmBeetleData, Integer>{
    List<BirmBeetleData> findBySpecies(TaxaSpecies species);
    BirmBeetleData findBySpeciesAndRowNumber(TaxaSpecies species, Short rowNumber);
}
