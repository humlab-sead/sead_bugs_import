package se.sead.bugsimport.species.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.species.bugsmodel.INDEX;
import se.sead.bugsimport.species.seadmodel.TaxaFamily;
import se.sead.bugsimport.species.seadmodel.TaxaGenus;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.repositories.SpeciesRepository;
import se.sead.utils.ErrorCopier;

@Component
public class TaxaSpeciesConverter {

    @Autowired
    private SpeciesRepository speciesRepository;
    @Autowired
    private TaxaGenusConverter genusConverter;
    @Autowired
    private TaxaAuthorConverter authorConverter;

    public TaxaSpecies convertToSeadType(INDEX bugsData) {
        if(bugsData.getSpecies() == null || bugsData.getSpecies().isEmpty()){
            return createErrorSpecies();
        }
        TaxaSpecies foundSpecies = speciesRepository.findBySpeciesNameAndGenusGenusNameAndTaxaAuthorAuthorName(bugsData.getSpecies(), bugsData.getGenus(), bugsData.getAuthority());
        if(foundSpecies == null){
            return createSpecies(bugsData);
        }
        return foundSpecies;
    }

    private TaxaSpecies createErrorSpecies(){
        TaxaSpecies error = new TaxaSpecies();
        error.addError("No Species name provided");
        return error;
    }

    private TaxaSpecies createSpecies(INDEX bugsData) {
        TaxaGenus taxaGenus = genusConverter.convertToSeadType(bugsData);
        TaxaSpecies species = new TaxaSpecies();
        species.setSpeciesName(bugsData.getSpecies());
        species.setGenus(taxaGenus);
        species.setTaxaAuthor(authorConverter.convertToSeadType(bugsData.getAuthority()));
        ErrorCopier.copyPotentialErrors(species, taxaGenus);
        return species;
    }
}
