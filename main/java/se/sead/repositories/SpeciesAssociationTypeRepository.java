package se.sead.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import se.sead.bugsimport.speciesassociation.seadmodel.SpeciesAssociationType;

public interface SpeciesAssociationTypeRepository extends Repository<SpeciesAssociationType, Integer>{

    SpeciesAssociationType findOne(Integer id);

    SpeciesAssociationType findByName(String name);

    @Query("select t from SpeciesAssociationType t where t.name = 'is associated with'")
    SpeciesAssociationType getDefaultType();
}
