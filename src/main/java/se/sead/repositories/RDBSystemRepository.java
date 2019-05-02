package se.sead.repositories;

import se.sead.bugsimport.rdbsystems.seadmodel.RdbSystem;

import java.util.List;

public interface RDBSystemRepository extends CreateAndReadRepository<RdbSystem, Integer>{
    List<RdbSystem> findAll();
}
