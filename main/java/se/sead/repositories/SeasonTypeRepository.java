package se.sead.repositories;

import org.springframework.data.repository.Repository;
import se.sead.bugsimport.taxaseasonality.seadmodel.SeasonType;

public interface SeasonTypeRepository extends Repository<SeasonType, Integer>{
    SeasonType getBySeasonType(String seasonType);
}
