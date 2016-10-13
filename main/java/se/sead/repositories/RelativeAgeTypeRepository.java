package se.sead.repositories;

import org.springframework.data.repository.Repository;
import se.sead.bugsimport.periods.seadmodel.RelativeAgeType;

public interface RelativeAgeTypeRepository extends Repository<RelativeAgeType, Integer> {
    RelativeAgeType findOne(Integer id);

    RelativeAgeType findByType(String type);
}
