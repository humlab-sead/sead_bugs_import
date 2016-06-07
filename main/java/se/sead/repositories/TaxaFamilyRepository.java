package se.sead.repositories;

import org.springframework.data.jpa.repository.Query;
import se.sead.bugsimport.species.seadmodel.TaxaFamily;
import se.sead.bugsimport.species.seadmodel.TaxaOrder;

/**
 * Created by erer0001 on 2016-04-22.
 */
public interface TaxaFamilyRepository extends CreateAndReadRepository<TaxaFamily, Integer> {
    @Query("select family from TaxaFamily family " +
            "where lower(family.familyName) = lower(?1) " +
            "and family.orderId.orderName = 'ORDER PENDING CLASSIFICATION'")
    TaxaFamily getBugsFamilyByName(String familyName);
    TaxaFamily findByFamilyNameAndOrderId(String familyName, TaxaOrder orderId);
}
