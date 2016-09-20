package se.sead.repositories;

import org.springframework.data.jpa.repository.Query;
import se.sead.bugsimport.ecocodedefinition.seadmodel.EcocodeGroup;

public interface EcocodeGroupRepository extends CreateAndReadRepository<EcocodeGroup, Integer>{

    @Query("select grp from EcocodeGroup grp where grp.label = 'Koch Group'")
    EcocodeGroup getKochGroup();
}
