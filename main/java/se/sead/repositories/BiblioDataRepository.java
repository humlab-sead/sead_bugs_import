package se.sead.repositories;

import se.sead.sead.model.Biblio;

/**
 * Created by erer0001 on 2016-05-18.
 */
public interface BiblioDataRepository  extends CreateAndReadRepository<Biblio, Integer>{
    Biblio getByBugsReferenceIgnoreCase(String bugsReference);
}
