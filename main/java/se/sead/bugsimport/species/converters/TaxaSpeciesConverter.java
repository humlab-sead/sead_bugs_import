package se.sead.bugsimport.species.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.species.bugsmodel.INDEX;
import se.sead.bugsimport.species.seadmodel.TaxaGenus;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.repositories.SpeciesRepository;

/**
 * Created by erer0001 on 2016-04-27.
 */
@Component
public class TaxaSpeciesConverter {

    @Autowired
    private SpeciesRepository speciesRepository;
    @Autowired
    private TaxaGenusConverter genusConverter;
    @Autowired
    private TaxaAuthorConverter authorConverter;

    public TaxaSpecies convertToSeadType(INDEX bugsData) {
        TaxaSpecies foundSpecies = speciesRepository.findByName(bugsData.getSpecies(), bugsData.getGenus());
        if(foundSpecies == null){
            return createSpecies(bugsData);
        }
        return foundSpecies;
    }

    private TaxaSpecies createSpecies(INDEX bugsData) {
        TaxaGenus taxaGenus = genusConverter.convertToSeadType(bugsData);
        TaxaSpecies species = new TaxaSpecies();
        species.setSpeciesName(bugsData.getSpecies());
        species.setGenus(taxaGenus);
        species.setTaxaAuthor(authorConverter.convertToSeadType(bugsData.getAuthority()));
        return species;
    }
}
