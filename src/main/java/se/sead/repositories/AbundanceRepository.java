package se.sead.repositories;

import se.sead.sead.data.Abundance;

import java.util.List;

public interface AbundanceRepository extends CreateAndReadRepository<Abundance, Integer> {

    List<Abundance> findAll();

}
