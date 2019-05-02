package se.sead.model;

import se.sead.sead.data.DataType;
import se.sead.sead.data.Dataset;
import se.sead.sead.data.DatasetMaster;
import se.sead.sead.methods.Method;

public class TestDataset extends Dataset {

    private TestDataset(Integer id){
        setId(id);
    }

    public static Dataset create(
            Integer id,
            String name,
            Method method,
            DatasetMaster masterDataset,
            DataType dataType
    ) {
        Dataset dataset = new TestDataset(id);
        dataset.setName(name);
        dataset.setMethod(method);
        dataset.setMasterDataset(masterDataset);
        dataset.setDataType(dataType);
        return dataset;
    }

    public static Dataset create(
            Integer id,
            String name,
            Method method,
            DatasetMaster masterDataset,
            DataType dataType,
            Dataset updatedDataset
    ){
        Dataset dataset = create(id, name, method, masterDataset, dataType);
        dataset.setUpdatedDataset(updatedDataset);
        return dataset;
    }
}
