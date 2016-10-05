package se.sead.repositories;

import se.sead.bugsimport.sample.seadmodel.Sample;

import java.util.List;

public interface SampleRepository extends CreateAndReadRepository<Sample, Integer> {
    List<Sample> findAll();
}
