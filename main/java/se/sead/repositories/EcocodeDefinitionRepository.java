package se.sead.repositories;

import se.sead.bugsimport.ecocodedefinition.seadmodel.EcocodeDefinition;

import java.util.List;

public interface EcocodeDefinitionRepository extends CreateAndReadRepository<EcocodeDefinition, Integer>{

    List<EcocodeDefinition> findAll();
}
