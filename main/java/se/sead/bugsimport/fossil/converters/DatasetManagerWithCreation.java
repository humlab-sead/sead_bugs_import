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
import se.sead.sead.data.Dataset;

import java.util.List;

@Component
public class DatasetManagerWithCreation extends AbstractDatasetManager{

    private final Dataset NO_DATA_TYPE_DATASET;

    private BugsTraceRepository traceRepository;
    private AbundanceMethodManager methodManager;
    private DatasetMasterRepository datasetMasterRepository;
    private DatasetCache datasetCache;

    @Autowired
    public DatasetManagerWithCreation(
            DataTypeRepository dataTypeRepository,
            DatasetRepository datasetRepository,
            AbundanceMethodManager methodManager,
            DatasetMasterRepository datasetMasterRepository,
            BugsTraceRepository traceRepository,
            DatasetCache datasetCache
    ){
        super(
                dataTypeRepository,
                datasetRepository,
                methodManager,
                datasetMasterRepository
        );
        this.traceRepository = traceRepository;
        this.methodManager = methodManager;
        this.datasetCache = datasetCache;
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
            dataset = createFor(valueCarrier);
            datasetCache.put(dataset);
        }
        return dataset;
    }

    @Override
    protected Dataset findDataset(DatasetData valueCarrier) {
        Dataset dataset = datasetCache.get(valueCarrier.getCountSheetCode());
        if(dataset != null){
            return dataset;
        }
        return super.findDataset(valueCarrier);
    }

    private Dataset createFor(DatasetData valueCarrier){
        Dataset dataset = new Dataset();
        dataset.setName(valueCarrier.getCountSheetCode());
        dataset.setDataType(valueCarrier.getDataType());
        dataset.setMethod(methodManager.getPalaeoentomology());
        dataset.setMasterDataset(datasetMasterRepository.findBugsMasterSet());
        return dataset;
    }

    public Dataset updateDataset(Dataset originalDataset){
        Dataset updatedDataset = new Dataset();
        updatedDataset.setUpdatedDataset(originalDataset);
        updatedDataset.setDataType(originalDataset.getDataType());
        updatedDataset.setMasterDataset(originalDataset.getMasterDataset());
        updatedDataset.setName(originalDataset.getName());
        updatedDataset.setMethod(originalDataset.getMethod());
        return updatedDataset;
    }

}
