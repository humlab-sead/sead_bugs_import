package se.sead.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import se.sead.bugsimport.locations.seadmodel.Location;
import se.sead.bugsimport.site.seadmodel.SeadSite;

import java.util.List;

public interface SiteRepository extends CreateAndReadRepository<SeadSite, Integer>{

    List<SeadSite> findAllByName(String name);
    List<SeadSite> findAllByNameStartingWithIgnoreCase(String name);

    default List<SeadSite> findByNameAndLocations(String name, List<Location> locations){
        return findByNameAndLocations(name, locations, (long)locations.size());
    }

    @Query("select site from SeadSite site " +
            "left join site.siteLocations siteLocations " +
            "where " +
            "site.name = :name and " +
            "siteLocations.location in :locations " +
            "group by site " +
            "having count(*) = :locationsize")
    List<SeadSite> findByNameAndLocations(
            @Param("name")String name,
            @Param("locations")List<Location> locations,
            @Param("locationsize") Long locationSize);
}
