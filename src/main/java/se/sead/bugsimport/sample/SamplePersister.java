package se.sead.bugsimport.sample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.Persister;
import se.sead.bugsimport.sample.bugsmodel.BugsSample;
import se.sead.bugsimport.sample.seadmodel.Sample;
import se.sead.bugsimport.sample.seadmodel.SampleDimension;
import se.sead.repositories.SampleDimensionRepository;
import se.sead.repositories.SampleRepository;

import java.util.ArrayList;
import java.util.List;

@Component
public class SamplePersister extends Persister<BugsSample, Sample> {

    @Autowired
    private SampleRepository repository;
    @Autowired
    private SampleDimensionRepository dimensionRepository;

    @Override
    protected Sample save(Sample seadValue) {
        Sample sample = repository.saveOrUpdate(seadValue);
        saveDimensions(sample, seadValue.getDimensions());
        return sample;
    }

    private void saveDimensions(Sample sample, List<SampleDimension> dimensions){
        if(dimensions == null){
            return;
        }
        List<SampleDimension> persistedSampleDimensions = new ArrayList<>();
        dimensions.forEach(
                dimension -> {
                    SampleDimension savedDimension = save(sample, dimension);
                    if(savedDimension != null){
                        persistedSampleDimensions.add(savedDimension);
                    }
                });
        sample.setDimensions(persistedSampleDimensions);
    }

    private SampleDimension save(Sample sample, SampleDimension dimension){
        dimension.setSample(sample);
        return dimensionRepository.saveOrUpdate(dimension);
    }
}
