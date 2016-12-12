package se.sead.bugsimport.species.converters;

import com.sun.org.apache.xerces.internal.impl.dv.dtd.NOTATIONDatatypeValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.species.bugsmodel.INDEX;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.repositories.SpeciesRepository;

@Component
public class NoDataSpeciesConverter {

    @Value("${no.data.code:9999.0000001}")
    private Double NO_DATA_CODE;

    @Autowired
    private SpeciesRepository repository;

    public boolean isNoDataSpecies(INDEX bugsData){
        return NO_DATA_CODE.equals(bugsData.getCode());
    }

    public TaxaSpecies getNoDataSpecies(){
        return repository.getBugsNoDataSpecies();
    }

    public boolean isNoDataSpecies(TaxaSpecies species){
        if(species.getId() != null){
            return getNoDataSpecies().getId().equals(species.getId());
        }
        return false;
    }
}
