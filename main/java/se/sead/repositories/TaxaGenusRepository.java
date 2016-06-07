package se.sead.repositories;

import se.sead.bugsimport.species.seadmodel.TaxaFamily;
import se.sead.bugsimport.species.seadmodel.TaxaGenus;

/**
 * Created by erer0001 on 2016-04-22.
 */
public interface TaxaGenusRepository extends CreateAndReadRepository<TaxaGenus, Integer> {
    TaxaGenus findByGenusNameAndTaxaFamily(String genusName, TaxaFamily taxaFamily);
}
