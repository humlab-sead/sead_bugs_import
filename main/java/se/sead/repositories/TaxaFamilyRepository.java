package se.sead.repositories;

import org.springframework.data.jpa.repository.Query;
import se.sead.bugsimport.species.seadmodel.TaxaFamily;
import se.sead.bugsimport.species.seadmodel.TaxaOrder;

public interface TaxaFamilyRepository extends CreateAndReadRepository<TaxaFamily, Integer> {
    @Query("select family from TaxaFamily family " +
            "where lower(family.familyName) = lower(?1) " +
            "and family.order.orderName = 'ORDER PENDING CLASSIFICATION'")
    TaxaFamily getBugsFamilyByName(String familyName);
    TaxaFamily findByFamilyNameAndOrder(String familyName, TaxaOrder orderId);
}
