package se.sead.repositories;

import se.sead.bugsimport.species.seadmodel.TaxaAuthor;

/**
 * Created by erer0001 on 2016-04-22.
 */
public interface TaxaAuthorRepository extends CreateAndReadRepository<TaxaAuthor, Integer> {
    TaxaAuthor findByAuthorName(String authorName);
}
