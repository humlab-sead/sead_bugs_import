package se.sead.repositories;

import se.sead.bugsimport.bibliography.seadmodel.Biblio;

import java.util.List;

public interface BiblioDataRepository  extends CreateAndReadRepository<Biblio, Integer>{
    Biblio getByBugsReferenceIgnoreCase(String bugsReference);
    List<Biblio> findAll();
}
