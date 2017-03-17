package se.sead.repositories;

import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.bugsimport.speciesbiology.seadmodel.TextBiology;
import se.sead.bugsimport.bibliography.seadmodel.Biblio;

import java.util.List;

public interface TextBiologyDataRepository extends CreateAndReadRepository<TextBiology, Integer>{
    List<TextBiology> findBySpecies(TaxaSpecies species);
    TextBiology findBySpeciesAndTextIgnoreCaseAndReference(TaxaSpecies species, String text, Biblio reference);

    List<TextBiology> findAll();
}
