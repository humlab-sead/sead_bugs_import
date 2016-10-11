package se.sead.repositories;

import se.sead.bugsimport.datesradio.seadmodel.Geochronology;

import java.util.List;

public interface GeochronologyRepository extends CreateAndReadRepository<Geochronology, Integer> {
    List<Geochronology> findAll();
}
