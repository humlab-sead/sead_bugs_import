package se.sead.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import se.sead.bugsimport.country.seadmodel.LocationType;

public interface LocationTypeRepository extends Repository<LocationType, Integer> {
    @Query("select type from LocationType type where type.name = 'Country'")
    LocationType getCountryType();
}
