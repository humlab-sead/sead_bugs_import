package se.sead.model;

import se.sead.bugsimport.sample.seadmodel.SampleDimension;
import se.sead.sead.methods.Method;
import se.sead.sead.model.Dimension;

import java.math.BigDecimal;

public class TestSampleDimension extends SampleDimension {

    private TestSampleDimension(Integer id){
        setId(id);
    }

    public static SampleDimension create(
            Integer id,
            Dimension dimension,
            Method method,
            BigDecimal value
    ){
        SampleDimension sampleDimension = new TestSampleDimension(id);
        sampleDimension.setDimension(dimension);
        sampleDimension.setMethod(method);
        sampleDimension.setValue(value);
        return sampleDimension;
    }
}
