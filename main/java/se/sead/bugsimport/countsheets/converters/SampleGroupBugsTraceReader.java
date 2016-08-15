package se.sead.bugsimport.countsheets.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.countsheets.bugsmodel.Countsheet;
import se.sead.bugsimport.countsheets.seadmodel.SampleGroup;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.repositories.BugsTraceRepository;
import se.sead.repositories.SampleGroupRepository;

import java.util.List;

@Component
public class SampleGroupBugsTraceReader {

    @Autowired
    private BugsTraceRepository traceRepository;
    @Autowired
    private SampleGroupRepository sampleGroupRepository;

    public SampleGroup getSampleGroup(Countsheet bugsData){
        List<BugsTrace> traces = traceRepository.findByBugsTableAndBugsIdentifierOrderByChangeDate(bugsData.bugsTable(), bugsData.getBugsIdentifier());
        if(traces.isEmpty()){
            return null;
        } else {
            return sampleGroupRepository.findOne(traces.get(0).getSeadId());
        }
    }
}
