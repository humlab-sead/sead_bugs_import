package se.sead.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import se.sead.bugsimport.ecocodedefinitiongroups.seadmodel.EcocodeSystem;

public interface EcocodeSystemRepository extends Repository<EcocodeSystem, Integer> {

    @Query("select eSystem from EcocodeSystem eSystem where eSystem.name = 'Koch System'")
    EcocodeSystem findKochSystem();
}
