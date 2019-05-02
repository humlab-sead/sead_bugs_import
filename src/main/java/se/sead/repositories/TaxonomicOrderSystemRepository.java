package se.sead.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import se.sead.bugsimport.species.seadmodel.TaxonomicOrderSystem;

/**
 * Created by erer0001 on 2016-04-22.
 */
public interface TaxonomicOrderSystemRepository extends Repository<TaxonomicOrderSystem, Integer> {
    @Query("select orderSystem from TaxonomicOrderSystem orderSystem where systemName = 'BugsCEP taxonomic order'")
    TaxonomicOrderSystem getBugsSystem();
}
