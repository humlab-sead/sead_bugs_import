package se.sead.model;

import se.sead.bugsimport.countsheets.seadmodel.SampleGroup;
import se.sead.bugsimport.sample.seadmodel.AlternativeReferenceType;
import se.sead.bugsimport.sample.seadmodel.Sample;
import se.sead.bugsimport.sample.seadmodel.SampleDimension;
import se.sead.bugsimport.sample.seadmodel.SampleType;

import java.util.List;

public class TestSample extends Sample {

    private TestSample(Integer id){
        setId(id);
    }

    public static Sample create(
            Integer id,
            String name,
            SampleGroup sampleGroup,
            AlternativeReferenceType referenceType,
            SampleType type
    ){
        Sample sample = new TestSample(id);
        sample.setName(name);
        sample.setGroup(sampleGroup);
        sample.setReferenceType(referenceType);
        sample.setType(type);
        return sample;
    }

    public static Sample create(
            Integer id,
            String name,
            SampleGroup sampleGroup,
            AlternativeReferenceType referenceType,
            SampleType type,
            List<SampleDimension> dimensions
    ){
        Sample sample = create(id, name, sampleGroup, referenceType, type);
        sample.setDimensions(dimensions);
        updateDimensionsWithSample(dimensions, sample);
        return sample;
    }

    private static void updateDimensionsWithSample(List<SampleDimension> dimensions, Sample sample) {
        dimensions.forEach(sampleDimension -> sampleDimension.setSample(sample));
    }
}
