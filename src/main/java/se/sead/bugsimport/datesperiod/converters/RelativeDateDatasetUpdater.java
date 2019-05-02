package se.sead.bugsimport.datesperiod.converters;

import se.sead.sead.data.DataType;
import se.sead.sead.data.Dataset;
import se.sead.sead.data.DatasetMaster;
import se.sead.sead.methods.Method;
import se.sead.utils.ErrorCopier;

import java.util.Objects;

public class RelativeDateDatasetUpdater {

    private DataType currentType;
    private Method bugsMethod;
    private String bugsName;
    private DatasetMaster masterSet;

    public RelativeDateDatasetUpdater(DataType dataType, Method bugsMethod, String bugsName, DatasetMaster masterSet){
        this.currentType = dataType;
        this.bugsMethod = bugsMethod;
        this.bugsName = bugsName;
        this.masterSet = masterSet;
    }

    public Dataset update(Dataset originalDataset){
         if(originalDataset == null){
            return createDataset();
        } else {
            return updateDataset(originalDataset);
        }
    }

    private Dataset createDataset(){
        return updateDataset(new Dataset());
    }

    private Dataset updateDataset(Dataset dataset) {
        boolean updated = setMethod(dataset);
        updated = setDataType(dataset) || updated;
        updated = setName(dataset) || updated;
        updated = setMasterDataset(dataset) || updated;
        if(updated){
            dataset.setUpdated(true);
        }
        return dataset;
    }

    private boolean setMethod(Dataset dataset) {
        Method originalMethod = dataset.getMethod();
        dataset.setMethod(bugsMethod);
        ErrorCopier.copyPotentialErrors(dataset, bugsMethod);
        return !Objects.equals(originalMethod, dataset.getMethod());
    }

    private boolean setDataType(Dataset dataset) {
        DataType originalType = dataset.getDataType();
        dataset.setDataType(currentType);
        return !Objects.equals(originalType, dataset.getDataType());
    }

    private boolean setName(Dataset dataset){
        String originalName = dataset.getName();
        dataset.setName(bugsName);
        return !Objects.equals(originalName, bugsName);
    }

    private boolean setMasterDataset(Dataset dataset){
        DatasetMaster originalMasterSet = dataset.getMasterDataset();
        dataset.setMasterDataset(masterSet);
        return !Objects.equals(originalMasterSet, dataset.getMasterDataset());
    }
}
