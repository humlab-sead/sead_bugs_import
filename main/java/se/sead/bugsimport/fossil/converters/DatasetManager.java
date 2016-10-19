package se.sead.bugsimport.fossil.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.countsheets.seadmodel.SampleGroup;
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
public class DatasetManager {

    private final Dataset NO_DATA_TYPE_DATASET;

    @Autowired
    private BugsTraceRepository traceRepository;
    @Autowired
    private DataTypeRepository dataTypeRepository;
    @Autowired
    private DatasetRepository datasetRepository;
    @Autowired
    private AbundanceMethodManager methodManager;

    private DatasetMaster bugsMasterSet;

    @Autowired
    public DatasetManager(DatasetMasterRepository datasetMasterRepository){
        NO_DATA_TYPE_DATASET = new Dataset();
        NO_DATA_TYPE_DATASET.addError("Unsupported countsheet data type");
        bugsMasterSet = datasetMasterRepository.findBugsMasterSet();
    }

    public Dataset getOrCreateFor(SampleGroup sampleGroup){
        List<BugsTrace> sampleGroupTraces = traceRepository.findBySeadTableAndSeadIdOrderByChangeDate("tbl_sample_groups", sampleGroup.getId());
        if(sampleGroupTraces.isEmpty()){
            throw new IllegalStateException("No sample group imported for supplied sample");
        }
        BugsTrace sampleGroupTrace = sampleGroupTraces.get(0);
        DatasetData valueCarrier = new DatasetData(sampleGroupTrace);
        if(valueCarrier.getDataType() == null){
            return NO_DATA_TYPE_DATASET;
        }
        Dataset dataset = findDataset(valueCarrier);
        if(dataset == null){
            return createFor(valueCarrier);
        }
        return dataset;
    }

    private Dataset findDataset(DatasetData valueCarrier){
        return datasetRepository.findByNameAndDataTypeAndMethodAndMasterDataset(
                valueCarrier.getCountSheetCode(),
                valueCarrier.getDataType(),
                methodManager.getPalaeoentomology(),
                bugsMasterSet);
    }

    private Dataset createFor(DatasetData valueCarrier){
        Dataset dataset = new Dataset();
        dataset.setName(valueCarrier.getCountSheetCode());
        dataset.setDataType(valueCarrier.getDataType());
        dataset.setMethod(methodManager.getPalaeoentomology());
        dataset.setMasterDataset(bugsMasterSet);
        return dataset;
    }

    private class DatasetData {
        private String countSheetCode;
        private DataType dataType;

        DatasetData(BugsTrace sampleGroupTrace){
            String bugsData = sampleGroupTrace.getCompressedBugsData().replace("{", "").replace("}", "");
            String[] components = bugsData.split(",");
            countSheetCode = components[0];
            String countSheetType = components[4];
            dataType = dataTypeRepository.findByName(countSheetType);
        }

        public String getCountSheetCode() {
            return countSheetCode;
        }

        public DataType getDataType() {
            return dataType;
        }
    }
}
