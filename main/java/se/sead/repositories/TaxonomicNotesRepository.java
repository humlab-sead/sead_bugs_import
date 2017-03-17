package se.sead.repositories;

import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.bugsimport.taxanotes.seadmodel.TaxonomicNotes;
import se.sead.bugsimport.bibliography.seadmodel.Biblio;

import java.util.List;

public interface TaxonomicNotesRepository extends CreateAndReadRepository<TaxonomicNotes, Integer>{

    TaxonomicNotes findByNotesAndSpeciesAndReference(String note, TaxaSpecies species, Biblio reference);

    List<TaxonomicNotes> findBySpecies(TaxaSpecies species);

    List<TaxonomicNotes> findAll();
}
