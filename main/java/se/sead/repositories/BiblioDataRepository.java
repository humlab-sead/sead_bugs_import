package se.sead.repositories;

import se.sead.sead.model.Biblio;

public interface BiblioDataRepository  extends CreateAndReadRepository<Biblio, Integer>{
    Biblio getByBugsReferenceIgnoreCase(String bugsReference);
}
