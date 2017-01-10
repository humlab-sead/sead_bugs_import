package se.sead.bugsimport.datasetcontacts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.countsheets.converters.SampleGroupBugsTraceReader;
import se.sead.bugsimport.fossil.converters.dataset.AbstractDatasetManager;
import se.sead.bugsimport.fossil.converters.dataset.AbundanceMethodManager;
import se.sead.bugsimport.fossil.converters.dataset.DatasetData;
import se.sead.bugsimport.tracing.SeadDataFromTraceHelper;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.repositories.DataTypeRepository;
import se.sead.repositories.DatasetMasterRepository;
import se.sead.repositories.DatasetRepository;
import se.sead.sead.data.Dataset;

@Component
public class DatasetManagerNoCreation extends AbstractDatasetManager {

    private final Dataset NO_SAMPLE_GROUP_TRACE_DATASET;
    private SampleGroupBugsTraceReader traceHelper;

    @Autowired
    public DatasetManagerNoCreation(
            SampleGroupBugsTraceReader traceHelper,
            DataTypeRepository dataTypeRepository,
            DatasetRepository datasetRepository,
            AbundanceMethodManager abundanceMethodManager,
            DatasetMasterRepository datasetMasterRepository) {
        super(
                dataTypeRepository,
                datasetRepository,
                abundanceMethodManager,
                datasetMasterRepository
        );
        this.traceHelper = traceHelper;
        NO_SAMPLE_GROUP_TRACE_DATASET = new Dataset();
        NO_SAMPLE_GROUP_TRACE_DATASET.addError("No samplegroup trace found");
    }

    public Dataset find(String countsheetCode){
        BugsTrace latest = traceHelper.getLatest(countsheetCode);
        if(latest != BugsTrace.NO_TRACE) {
            DatasetData datasetData = build(latest);
            return findDataset(datasetData);
        } else {
            return NO_SAMPLE_GROUP_TRACE_DATASET;
        }
    }
}
