package se.sead.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import se.sead.bugsimport.taxaseasonality.seadmodel.ActivityType;

public interface ActivityTypeRepository extends Repository<ActivityType, Integer>{

    @Query("select type from ActivityType type where type.name = 'Adult active'")
    ActivityType findAdultActiveType();
}
