package se.sead.bugsimport.fossil.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.countsheets.seadmodel.SampleGroup;
import se.sead.bugsimport.fossil.converters.dataset.AbstractDatasetManager;
import se.sead.bugsimport.fossil.converters.dataset.AbundanceMethodManager;
import se.sead.bugsimport.fossil.converters.dataset.DatasetData;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.repositories.BugsTraceRepository;
import se.sead.repositories.DataTypeRepository;
import se.sead.repositories.DatasetMasterRepository;
import se.sead.repositories.DatasetRepository;
import se.sead.sead.data.DataType;
import se.sead.sead.data.Dataset;
import se.sead.sead.data.DatasetMaster;

import java.util.List;

@Component
public class DatasetManagerWithCreation extends AbstractDatasetManager{

    private final Dataset NO_DATA_TYPE_DATASET;

    private BugsTraceRepository traceRepository;
    private AbundanceMethodManager methodManager;
    private DatasetMasterRepository datasetMasterRepository;

    @Autowired
    public DatasetManagerWithCreation(
            DataTypeRepository dataTypeRepository,
            DatasetRepository datasetRepository,
            AbundanceMethodManager methodManager,
            DatasetMasterRepository datasetMasterRepository,
            BugsTraceRepository traceRepository
    ){
        super(
                dataTypeRepository,
                datasetRepository,
                methodManager,
                datasetMasterRepository
        );
        this.traceRepository = traceRepository;
        this.methodManager = methodManager;
        NO_DATA_TYPE_DATASET = new Dataset();
        NO_DATA_TYPE_DATASET.addError("Unsupported countsheet data type");
        this.datasetMasterRepository = datasetMasterRepository;
    }

    public Dataset getOrCreateFor(SampleGroup sampleGroup){
        List<BugsTrace> sampleGroupTraces = traceRepository.findBySeadTableAndSeadIdOrderByChangeDate("tbl_sample_groups", sampleGroup.getId());
        if(sampleGroupTraces.isEmpty()){
            throw new IllegalStateException("No sample group imported for supplied sample");
        }
        BugsTrace sampleGroupTrace = sampleGroupTraces.get(0);
        DatasetData valueCarrier = build(sampleGroupTrace);
        if(valueCarrier.getDataType() == null){
            return NO_DATA_TYPE_DATASET;
        }
        Dataset dataset = findDataset(valueCarrier);
        if(dataset == null){
            return createFor(valueCarrier);
        }
        return dataset;
    }

    private Dataset createFor(DatasetData valueCarrier){
        Dataset dataset = new Dataset();
        dataset.setName(valueCarrier.getCountSheetCode());
        dataset.setDataType(valueCarrier.getDataType());
        dataset.setMethod(methodManager.getPalaeoentomology());
        dataset.setMasterDataset(datasetMasterRepository.findBugsMasterSet());
        return dataset;
    }
}
