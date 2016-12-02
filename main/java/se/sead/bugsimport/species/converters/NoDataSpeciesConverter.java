package se.sead.bugsimport.species.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.species.bugsmodel.INDEX;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.repositories.SpeciesRepository;

@Component
public class NoDataSpeciesConverter {

    private static final Double NO_DATA_CODE = new Double(9999d);

    @Autowired
    private SpeciesRepository repository;

    public boolean isNoDataSpecies(INDEX bugsData){
        return NO_DATA_CODE.equals(bugsData.getCode());
    }

    public TaxaSpecies getNoDataSpecies(){
        return repository.getBugsNoDataSpecies();
    }
}
