package se.sead.repositories;

import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.bugsimport.specieskeys.seadmodel.TextIdentificationKeys;
import se.sead.bugsimport.bibliography.seadmodel.Biblio;

import java.util.List;

public interface TextIdentificationKeysRepository extends CreateAndReadRepository<TextIdentificationKeys, Integer> {
    List<TextIdentificationKeys> findAll();
    List<TextIdentificationKeys> findBySpecies(TaxaSpecies species);
    TextIdentificationKeys findByKeysAndSpeciesAndReference(String keys, TaxaSpecies species, Biblio reference);
}
