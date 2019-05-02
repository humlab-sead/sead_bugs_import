package se.sead.repositories;

import se.sead.bugsimport.periods.seadmodel.RelativeAge;

import java.util.List;

public interface RelativeAgeRepository extends CreateAndReadRepository<RelativeAge, Integer> {
    List<RelativeAge> findAll();

    RelativeAge findByAbbreviation(String abbreviation);
}
