package se.sead.repositories;

import se.sead.bugsimport.mcrnames.seadmodel.MCRName;

import java.util.List;

public interface McrNamesRepository  extends CreateAndReadRepository<MCRName, Integer>{
    List<MCRName> findAll();
}
