package se.sead.bugsimport.speciessynonyms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.speciesassociation.seadmodel.SpeciesAssociationType;
import se.sead.repositories.SpeciesAssociationTypeRepository;

@Component
public class SynonymAssociationTypeManager {

    @Autowired
    private SpeciesAssociationTypeRepository typeRepository;

    @Value("${synonym.assocation.type.name:synonym of}")
    private String synonymAssociationTypeName;
    private SpeciesAssociationType synonymType;

    public SpeciesAssociationType getSynonymType(){
        if(synonymType == null){
            synonymType = typeRepository.findByName(synonymAssociationTypeName);
            assert synonymType != null;
        }
        return synonymType;
    }
}
