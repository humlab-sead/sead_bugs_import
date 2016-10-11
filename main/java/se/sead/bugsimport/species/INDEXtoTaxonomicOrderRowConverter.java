package se.sead.bugsimport.species;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.BugsTableRowConverter;
import se.sead.bugsimport.species.bugsmodel.INDEX;
import se.sead.bugsimport.species.converters.TaxaSpeciesConverter;
import se.sead.bugsimport.species.converters.TaxonomicOrderConverter;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.bugsimport.species.seadmodel.TaxonomicOrder;

/**
 * Created by erer0001 on 2016-04-27.
 */
@Component
public class INDEXtoTaxonomicOrderRowConverter implements BugsTableRowConverter<INDEX, TaxonomicOrder> {

    @Autowired
    private TaxonomicOrderConverter taxonomicOrderConverter;
    @Autowired
    private TaxaSpeciesConverter taxaConverter;

    @Override
    public TaxonomicOrder convertForDataRow(INDEX bugsData) {
        TaxonomicOrder taxonomicOrder = convertOrCreateOrderByTaxonomicCode(bugsData);
        if(isNewOrder(taxonomicOrder)){
            addSpecies(taxonomicOrder, bugsData);
        }
        return taxonomicOrder;
    }

    private TaxonomicOrder convertOrCreateOrderByTaxonomicCode(INDEX bugsData) {
        return taxonomicOrderConverter.convertToSeadType(bugsData.getCode());
    }

    private boolean isNewOrder(TaxonomicOrder taxonomicOrder) {
        return taxonomicOrder.isNewItem();
    }

    private void addSpecies(TaxonomicOrder taxonomicOrder, INDEX bugsData) {
        TaxaSpecies species = taxaConverter.convertToSeadType(bugsData);
        taxonomicOrder.setSpecies(species);
    }
}
