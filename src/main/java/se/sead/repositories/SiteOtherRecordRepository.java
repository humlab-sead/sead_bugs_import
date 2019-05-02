package se.sead.repositories;

import org.springframework.stereotype.Repository;
import se.sead.bugsimport.site.seadmodel.SeadSite;
import se.sead.bugsimport.siteotherproxies.seadmodel.SiteOtherRecord;

import java.util.List;

@Repository
public interface SiteOtherRecordRepository extends CreateAndReadRepository<SiteOtherRecord, Integer> {

    List<SiteOtherRecord> findAllBySite(SeadSite site);
}
