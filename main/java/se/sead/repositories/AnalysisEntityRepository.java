package se.sead.repositories;

import org.springframework.data.repository.Repository;
import se.sead.bugsimport.sample.seadmodel.Sample;
import se.sead.sead.data.AnalysisEntity;
import se.sead.sead.data.Dataset;

import java.util.List;

public interface AnalysisEntityRepository extends Repository<AnalysisEntity, Integer> {

    List<AnalysisEntity> findBySampleAndDataset(Sample sample, Dataset dataset);
}
