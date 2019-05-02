package se.sead.bugsimport.species.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.species.bugsmodel.INDEX;
import se.sead.bugsimport.species.seadmodel.TaxaFamily;
import se.sead.bugsimport.species.seadmodel.TaxaGenus;
import se.sead.repositories.TaxaGenusRepository;
import se.sead.utils.ErrorCopier;

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
            genus = createGenus(bugsData, family);
        }
        ErrorCopier.copyPotentialErrors(genus, family);
        if(bugsData.getGenus() == null || bugsData.getGenus().isEmpty()){
            genus.addError("No genus specified");
        }
        return genus;
    }

    private TaxaGenus getExistingGenus(INDEX bugsData, TaxaFamily family) {
        if(family.isNewItem() || !family.isErrorFree()){
            return null;
        }
        return genusRepository.findByGenusNameAndFamily(bugsData.getGenus(), family);
    }

    private boolean isNewFamily(TaxaFamily family) {
        return family.getId() == null;
    }

    private TaxaGenus createGenus(INDEX bugsData, TaxaFamily family) {
        TaxaGenus genus = new TaxaGenus();
        genus.setGenusName(bugsData.getGenus());
        genus.setFamily(family);
        return genus;
    }
}
