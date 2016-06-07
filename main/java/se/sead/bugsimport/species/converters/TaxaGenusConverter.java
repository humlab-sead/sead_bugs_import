package se.sead.bugsimport.species.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.species.bugsmodel.INDEX;
import se.sead.bugsimport.species.seadmodel.TaxaFamily;
import se.sead.bugsimport.species.seadmodel.TaxaGenus;
import se.sead.repositories.TaxaGenusRepository;

/**
 * Created by erer0001 on 2016-04-27.
 */
@Component
public class TaxaGenusConverter{

    @Autowired
    private TaxaGenusRepository genusRepository;
    @Autowired
    private TaxaFamilyConverter familyConverter;

    public TaxaGenus convertToSeadType(INDEX bugsData) {
        TaxaFamily family = familyConverter.convertToSeadType(bugsData.getFamily());
        return getOrCreateGenus(bugsData, family);
    }

    private TaxaGenus getOrCreateGenus(INDEX bugsData, TaxaFamily family) {
        TaxaGenus genus = getExistingGenus(bugsData, family);
        if(genus == null){
            return createGenus(bugsData, family);
        } else {
            return genus;
        }
    }

    private TaxaGenus getExistingGenus(INDEX bugsData, TaxaFamily family) {
        if(isNewFamily(family)){
            return null;
        }
        return genusRepository.findByGenusNameAndTaxaFamily(bugsData.getGenus(), family);
    }

    private boolean isNewFamily(TaxaFamily family) {
        return family.getId() == null;
    }

    private TaxaGenus createGenus(INDEX bugsData, TaxaFamily family) {
        TaxaGenus genus = new TaxaGenus();
        genus.setGenusName(bugsData.getGenus());
        genus.setTaxaFamily(family);
        return genus;
    }
}
