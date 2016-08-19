package se.sead.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import se.sead.bugsimport.locations.seadmodel.LocationType;

public interface LocationTypeRepository extends Repository<LocationType, Integer> {

    @Query("select type from LocationType type where type.name = 'Country'")
    LocationType getCountryType();

    @Query("select type from LocationType type where type.name = 'Sub-country administrative region'")
    LocationType getAdministrativeRegionType();

    @Query("select type from LocationType type where type.name = 'Aggregate/non-admin geographical region'")
    LocationType getAggregateNonAdministrativeRegionType();
}
