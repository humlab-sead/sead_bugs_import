package se.sead.bugsimport.taxaseasonality.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.species.converters.TaxonomicOrderConverter;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.bugsimport.species.seadmodel.TaxonomicOrder;
import se.sead.bugsimport.taxaseasonality.bugsmodel.SeasonActiveAdult;

@Component
public class SeasonActiveAdultSpeciesConverter {

    @Autowired
    private TaxonomicOrderConverter orderConverter;

    private TaxaSpecies noSpeciesFound;

    public SeasonActiveAdultSpeciesConverter(){
        noSpeciesFound = new TaxaSpecies();
        noSpeciesFound.addError("No species found");
    }

    public TaxaSpecies getSpecies(SeasonActiveAdult bugsData){
        TaxonomicOrder taxonomicOrder = orderConverter.convertToSeadType(bugsData.getCode());
        if(taxonomicOrder.isNewItem()){
            return noSpeciesFound;
        }
        TaxaSpecies bugsSpeciesByCode = taxonomicOrder.getSpecies();
        if(bugsSpeciesByCode == null){
            return noSpeciesFound;
        }
        return bugsSpeciesByCode;
    }
}
