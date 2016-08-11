package se.sead.repositories;

import org.springframework.stereotype.Repository;
import se.sead.bugsimport.site.seadmodel.SeadSite;
import se.sead.bugsimport.sitereferences.seadmodel.SiteReference;
import se.sead.sead.model.Biblio;

import java.util.List;

@Repository
public interface SiteReferenceRepository extends CreateAndReadRepository<SiteReference, Integer>{
    SiteReference findBySiteAndReference(SeadSite site, Biblio reference);
    List<SiteReference> findAllBySite(SeadSite site);
}
