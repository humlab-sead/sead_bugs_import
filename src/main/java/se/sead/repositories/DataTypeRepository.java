package se.sead.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import se.sead.sead.data.DataType;

public interface DataTypeRepository extends Repository<DataType, Integer>{
    DataType findOne(Integer id);

    @Query("select dataType from DataType dataType where dataType.name = 'Undefined other'")
    DataType findBugsGeochronologyDataType();

    DataType findByName(String name);
}
