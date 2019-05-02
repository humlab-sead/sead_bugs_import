package se.sead.repositories;

import org.springframework.data.repository.Repository;
import se.sead.bugsimport.taxaseasonality.seadmodel.Season;
import se.sead.bugsimport.taxaseasonality.seadmodel.SeasonType;

public interface SeasonRepository extends Repository<Season, Integer>{
    Season findOne(Integer id);
    Season findByNameAndType(String name, SeasonType type);
}
