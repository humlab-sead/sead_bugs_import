package se.sead.repositories;

import se.sead.bugsimport.translations.model.IdBasedTranslation;

import java.util.List;

public interface IdBasedTranslationRepository extends CreateAndReadRepository<IdBasedTranslation, Integer> {

    List<IdBasedTranslation> getAllByBugsDefinitionAndBugsTable(String compressedSolution, String bugsTable);
}
