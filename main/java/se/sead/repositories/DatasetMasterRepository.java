package se.sead.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import se.sead.sead.data.DatasetMaster;

public interface DatasetMasterRepository extends Repository<DatasetMaster, Integer>{
    DatasetMaster findOne(Integer id);

    @Query("select masterSet from DatasetMaster masterSet where " +
            "masterSet.name = 'Bugs database'")
    DatasetMaster findBugsMasterSet();
}
