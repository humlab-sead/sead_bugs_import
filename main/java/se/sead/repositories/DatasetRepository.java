package se.sead.repositories;

import org.springframework.data.repository.Repository;
import se.sead.sead.data.DataType;
import se.sead.sead.data.Dataset;
import se.sead.sead.data.DatasetMaster;
import se.sead.sead.methods.Method;

import java.util.List;

public interface DatasetRepository extends Repository<Dataset, Integer> {
    Dataset findByNameAndDataTypeAndMethodAndMasterDataset(String name, DataType dataType, Method method, DatasetMaster datasetMaster);
    List<Dataset> findAll();
}
