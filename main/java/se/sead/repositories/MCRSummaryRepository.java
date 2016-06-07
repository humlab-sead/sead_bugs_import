package se.sead.repositories;

import se.sead.bugsimport.mcr.seadmodel.MCRSummary;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;

public interface MCRSummaryRepository extends CreateAndReadRepository<MCRSummary, Integer> {
    MCRSummary findBySpecies(TaxaSpecies species);
}
