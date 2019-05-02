package se.sead.repositories;

import se.sead.bugsimport.ecocodes.seadmodel.Ecocode;

import java.util.List;

public interface EcocodeRepository extends CreateAndReadRepository<Ecocode, Integer> {

    List<Ecocode> findAll();

}
