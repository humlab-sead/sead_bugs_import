package se.sead.repositories;

import se.sead.bugsimport.species.seadmodel.TaxaFamily;
import se.sead.bugsimport.species.seadmodel.TaxaGenus;

public interface TaxaGenusRepository extends CreateAndReadRepository<TaxaGenus, Integer> {
    TaxaGenus findByGenusNameAndFamily(String genusName, TaxaFamily taxaFamily);
}
