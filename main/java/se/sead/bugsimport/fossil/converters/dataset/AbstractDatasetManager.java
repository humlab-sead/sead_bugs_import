package se.sead.bugsimport.fossil.converters.dataset;

import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.repositories.DataTypeRepository;
import se.sead.repositories.DatasetMasterRepository;
import se.sead.repositories.DatasetRepository;
import se.sead.sead.data.DataType;
import se.sead.sead.data.Dataset;
import se.sead.sead.data.DatasetMaster;
import se.sead.sead.methods.Method;

public class AbstractDatasetManager {

    private DataTypeRepository dataTypeRepository;
    private DatasetRepository datasetRepository;
    private AbundanceMethodManager methodManager;
    private DatasetMasterRepository datasetMasterRepository;

    protected AbstractDatasetManager(
            DataTypeRepository dataTypeRepository,
            DatasetRepository datasetRepository,
            AbundanceMethodManager abundanceMethodManager,
            DatasetMasterRepository datasetMasterRepository
    ){
        this.dataTypeRepository = dataTypeRepository;
        this.datasetRepository = datasetRepository;
        this.methodManager = abundanceMethodManager;
        this.datasetMasterRepository = datasetMasterRepository;
    }

    protected Dataset findDataset(DatasetData valueCarrier){
        return datasetRepository.findByNameAndDataTypeAndMethodAndMasterDataset(
                valueCarrier.getCountSheetCode(),
                valueCarrier.getDataType(),
                methodManager.getPalaeoentomology(),
                datasetMasterRepository.findBugsMasterSet());
    }

    protected DatasetData build(BugsTrace sampleGroupTrace){
        String bugsData = sampleGroupTrace.getCompressedBugsData().replace("{", "").replace("}", "");
        String[] components = bugsData.split(",");
        String countSheetCode = components[0];
        String countSheetType = components[4];
        DataType dataType = dataTypeRepository.findByName(countSheetType);
        return new DatasetData(countSheetCode, dataType);
    }

}
