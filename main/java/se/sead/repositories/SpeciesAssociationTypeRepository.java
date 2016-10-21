package se.sead.repositories;

import org.springframework.data.repository.Repository;
import se.sead.bugsimport.speciesassociation.seadmodel.SpeciesAssociationType;

public interface SpeciesAssociationTypeRepository extends Repository<SpeciesAssociationType, Integer>{

    SpeciesAssociationType findOne(Integer id);

    SpeciesAssociationType findByName(String name);
}
