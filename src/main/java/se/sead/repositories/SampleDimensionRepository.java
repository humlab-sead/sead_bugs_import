package se.sead.repositories;

import se.sead.bugsimport.sample.seadmodel.Sample;
import se.sead.bugsimport.sample.seadmodel.SampleDimension;

import java.util.List;

public interface SampleDimensionRepository  extends CreateAndReadRepository<SampleDimension, Integer>{
    List<SampleDimension> findBySample(Sample sample);
}
