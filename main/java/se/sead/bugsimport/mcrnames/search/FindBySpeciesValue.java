package se.sead.bugsimport.mcrnames.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.mcrnames.bugsmodel.BugsMCRNames;
import se.sead.bugsimport.mcrnames.seadmodel.MCRName;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.repositories.McrNamesRepository;
import se.sead.repositories.TaxonomicOrderRepository;
import se.sead.utils.BigDecimalDefinition;

@Component
@Order(2)
public class FindBySpeciesValue implements MCRSearch {

    @Autowired
    private TaxonomicOrderRepository taxonomicOrderRepository;
    @Autowired
    private McrNamesRepository repository;

    @Override
    public MCRName findFor(BugsMCRNames bugsData) {
        TaxaSpecies bugsSpeciesByCode = taxonomicOrderRepository.findBugsSpeciesByCode(BigDecimalDefinition.convertToSeadCode(bugsData.getCode()));
        if(bugsSpeciesByCode == null){
            return NO_MCR_NAME_FOUND;
        }
        MCRName bySpecies = repository.findOne(bugsSpeciesByCode.getId());
        if(bySpecies == null){
            return NO_MCR_NAME_FOUND;
        }
        return bySpecies;
    }
}
