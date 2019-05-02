package se.sead.repositories;

import se.sead.bugsimport.fossil.seadmodel.Abundance;

import java.util.List;

public interface AbundanceRepository extends CreateAndReadRepository<Abundance, Integer> {

    List<Abundance> findAll();

}
