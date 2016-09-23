package se.sead.repositories;

import se.sead.bugsimport.rdbcodes.seadmodel.RdbCode;
import se.sead.bugsimport.rdbsystems.seadmodel.RdbSystem;

import java.util.List;

public interface RdbCodeRepository extends CreateAndReadRepository<RdbCode, Integer> {
    List<RdbCode> findAll();
    List<RdbCode> findByCategoryAndDefinitionAndSystem(String category, String definition, RdbSystem system);
}
