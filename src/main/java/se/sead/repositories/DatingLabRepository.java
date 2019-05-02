package se.sead.repositories;

import se.sead.bugsimport.lab.seadmodel.DatingLab;

import java.util.List;

public interface DatingLabRepository extends CreateAndReadRepository<DatingLab, Integer>{
    List<DatingLab> findAll();

    DatingLab findByLabId(String labId);
}
