package se.sead.repositories;

import org.springframework.data.jpa.repository.Query;
import se.sead.bugsimport.country.seadmodel.Location;
import se.sead.bugsimport.country.seadmodel.LocationType;

import java.util.List;

public interface LocationRepository extends CreateAndReadRepository<Location, Integer>{
    @Query("select location from Location location where location.name = ?1 and location.type.name = 'Country'")
    Location findCountryByName(String countryName);
    List<Location> findByTypeOrderByName(LocationType type);

    List<Location> findAllByName(String name);
}
