package se.sead.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import se.sead.bugsimport.sample.seadmodel.SampleType;

public interface SampleTypeRepository extends Repository<SampleType, Integer>{

    @Query("select type from SampleType type where name = 'Unspecified'")
    SampleType getUnspecifiedType();

    SampleType findOne(Integer id);
}
