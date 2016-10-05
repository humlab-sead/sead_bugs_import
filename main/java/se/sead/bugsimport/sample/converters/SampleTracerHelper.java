package se.sead.bugsimport.sample.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.sample.bugsmodel.BugsSample;
import se.sead.bugsimport.sample.bugsmodel.SampleBugsTable;
import se.sead.bugsimport.sample.seadmodel.Sample;
import se.sead.bugsimport.tracing.SeadDataFromTraceHelper;
import se.sead.repositories.SampleRepository;

@Component
public class SampleTracerHelper extends SeadDataFromTraceHelper<BugsSample, Sample> {

    @Autowired
    public SampleTracerHelper(SampleRepository sampleRepository){
        super(SampleBugsTable.TABLE_NAME, "tbl_physical_samples", false, sampleRepository);
    }
}
