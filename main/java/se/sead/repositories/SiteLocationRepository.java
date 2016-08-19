package se.sead.repositories;

import se.sead.bugsimport.site.seadmodel.SeadSite;
import se.sead.bugsimport.sitelocations.seadmodel.SiteLocation;

import java.util.List;

public interface SiteLocationRepository extends CreateAndReadRepository<SiteLocation, Integer> {
    List<SiteLocation> findBySite(SeadSite site);
}
