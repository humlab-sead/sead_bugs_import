package se.sead.repositories;

import se.sead.bugsimport.speciesassociation.seadmodel.SpeciesAssociation;

import java.util.List;

public interface SpeciesAssociationRepository extends CreateAndReadRepository<SpeciesAssociation, Integer> {

    List<SpeciesAssociation> findAll();

}
