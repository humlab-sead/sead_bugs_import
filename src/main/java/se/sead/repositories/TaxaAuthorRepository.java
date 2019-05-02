package se.sead.repositories;

import se.sead.bugsimport.species.seadmodel.TaxaAuthor;

import java.util.List;

public interface TaxaAuthorRepository extends CreateAndReadRepository<TaxaAuthor, Integer> {
    TaxaAuthor findByAuthorName(String authorName);

    List<TaxaAuthor> findAll();
}
