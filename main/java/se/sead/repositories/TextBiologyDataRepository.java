package se.sead.repositories;

import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.bugsimport.speciesbiology.seadmodel.TextBiology;
import se.sead.sead.model.Biblio;

import java.util.List;

/**
 * Created by erer0001 on 2016-05-18.
 */
public interface TextBiologyDataRepository extends CreateAndReadRepository<TextBiology, Integer>{
    List<TextBiology> findBySpecies(TaxaSpecies species);
    TextBiology findBySpeciesAndTextIgnoreCaseAndReference(TaxaSpecies species, String text, Biblio reference);
}
