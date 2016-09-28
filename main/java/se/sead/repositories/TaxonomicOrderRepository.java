package se.sead.repositories;

import org.springframework.data.jpa.repository.Query;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.bugsimport.species.seadmodel.TaxonomicOrder;
import se.sead.bugsimport.species.seadmodel.TaxonomicOrderSystem;
import se.sead.utils.BigDecimalDefinition;

import java.math.BigDecimal;

/**
 * Created by erer0001 on 2016-04-22.
 */
public interface TaxonomicOrderRepository extends CreateAndReadRepository<TaxonomicOrder, Integer> {

    String WHERE_TAXA_ORDER_SYSTEM_IS_BUGS_SYSTEM =
            " and taxaOrder.orderSystem.systemName = 'BugsCEP taxonomic order'";

    @Query("select taxaOrder from TaxonomicOrder taxaOrder where taxaOrder.code = ?1" + WHERE_TAXA_ORDER_SYSTEM_IS_BUGS_SYSTEM)
    TaxonomicOrder findBugsCodeByCode(BigDecimal code);

    TaxonomicOrder findByCodeAndOrderSystem(BigDecimal code, TaxonomicOrderSystem orderSystem);

    @Query("select taxaOrder.species from TaxonomicOrder taxaOrder where taxaOrder.code = ?1" + WHERE_TAXA_ORDER_SYSTEM_IS_BUGS_SYSTEM)
    TaxaSpecies findBugsSpeciesByCode(BigDecimal code);

    TaxonomicOrder save(TaxonomicOrder taxonomicOrder);

    default TaxaSpecies findBugsSpeciesByCode(Double bugsCode){
        if(bugsCode == null){
            return null;
        }
        return findBugsSpeciesByCode(BigDecimalDefinition.convertToSeadCode(bugsCode));
    }
}
