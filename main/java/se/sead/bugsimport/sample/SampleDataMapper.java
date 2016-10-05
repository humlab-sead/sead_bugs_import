package se.sead.bugsimport.sample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.BugsSeadMapper;
import se.sead.bugsimport.sample.bugsmodel.BugsSample;
import se.sead.bugsimport.sample.bugsmodel.SampleBugsTable;
import se.sead.bugsimport.sample.seadmodel.Sample;

@Component
public class SampleDataMapper extends BugsSeadMapper<BugsSample, Sample> {

    @Autowired
    public SampleDataMapper(
        SampleRowConverter rowConverter
    ){
        super(new SampleBugsTable(), rowConverter);
    }
}
