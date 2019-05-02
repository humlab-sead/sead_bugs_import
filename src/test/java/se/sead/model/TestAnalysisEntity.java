package se.sead.model;

import se.sead.bugsimport.sample.seadmodel.Sample;
import se.sead.sead.data.AnalysisEntity;
import se.sead.sead.data.Dataset;

public class TestAnalysisEntity extends AnalysisEntity {

    private TestAnalysisEntity(Integer id){
        setId(id);
    }

    public static AnalysisEntity create(
            Integer id,
            Dataset dataset,
            Sample sample
    ){
        AnalysisEntity ae = new TestAnalysisEntity(id);
        ae.setSample(sample);
        ae.setDataset(dataset);
        return ae;
    }
}
