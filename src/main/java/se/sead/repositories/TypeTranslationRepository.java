package se.sead.repositories;

import se.sead.bugsimport.translations.model.TypeTranslation;

import java.util.List;

public interface TypeTranslationRepository extends CreateAndReadRepository<TypeTranslation, Integer> {
    List<TypeTranslation> getByBugsTable(String bugsTable);
}
