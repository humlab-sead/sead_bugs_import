package se.sead.repositories;

import se.sead.bugsimport.ecocodedefinitiongroups.seadmodel.EcocodeGroup;
import se.sead.bugsimport.ecocodedefinitiongroups.seadmodel.EcocodeSystem;

import java.util.List;

public interface EcocodeGroupRepository extends CreateAndReadRepository<EcocodeGroup, Integer>{

    List<EcocodeGroup> findBySystem(EcocodeSystem system);

    EcocodeGroup findBySystemAndAbbreviation(EcocodeSystem kochSystem, String abbreviation);
}
